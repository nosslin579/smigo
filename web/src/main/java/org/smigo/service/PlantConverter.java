package org.smigo.service;

import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.smigo.entities.PlantDataBean;
import kga.Plant;
import kga.Square;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class PlantConverter {

  private static final Logger log = LoggerFactory.getLogger(PlantConverter.class);

  private static ObjectMapper mapper = new ObjectMapper();
  private static final int FIELD_START = 438351867;
  private static final int FIELD_SQUARE = 869725394;
  private static final int FIELD_ADDRESS = 495410209;
  private static final int KGA_FILE_VERSION = 1;

  private PlantConverter() {
  }

  public static List<PlantDataBean> convert(String jsonPlants) throws JsonParseException,
                                                                  JsonMappingException, IOException {
    List<Map<String, Integer>> jsonPlantList = mapper.readValue(jsonPlants, ArrayList.class);
    int year = jsonPlantList.get(0).get("year");
    jsonPlantList.remove(0);
    List<PlantDataBean> ret = new ArrayList<PlantDataBean>();
    for (Map<String, Integer> pl : jsonPlantList) {
      int id = pl.get("a");
      int x = pl.get("x");
      int y = pl.get("y");
      ret.add(new PlantDataBean(id, year, x, y));
    }
    return ret;
  }

  public static List<PlantDataBean> convert(MultipartFile file) throws IOException {
    List<PlantDataBean> ret = new ArrayList<PlantDataBean>();
    DataInputStream in = new DataInputStream(new BufferedInputStream(file.getInputStream()));
    int year = 0, gridx = 0, gridy = 0, field;
    try {
      if (in.readInt() != FIELD_START) {
        in.close();
        throw new IllegalStateException("Not a valid kga file");
      } else if (in.readInt() > KGA_FILE_VERSION) {
        in.close();
        throw new IllegalStateException("Program version older than file version");
      }

      while (true) {
        field = in.readInt();
        if (field == FIELD_SQUARE) {
          year = in.readInt();
          gridx = in.readInt();
          gridy = in.readInt();
        } else if (field == FIELD_ADDRESS) {
          int species = in.readInt();
          // replace path vertical and horizontal with crossing
          if (species == 117 || species == 119)
            species = 116;

          ret.add(new PlantDataBean(species, year, gridx, gridy));
        }
      }
    } catch (EOFException e) {
    } finally {
      in.close();
    }
    log.debug("Converted file to " + ret.size() + " plants");
    return ret;
  }

  public static List<PlantDataBean> convert(Collection<Square> squares) {
    List<PlantDataBean> ret = new ArrayList<PlantDataBean>();
    for (Square s : squares) {
      for (Plant p : s.getPlants()) {
        ret.add(new PlantDataBean(p.getSpecies().getId(), s.getYear(), s.getX(), s.getY()));
      }
    }
    return ret;
  }
}
