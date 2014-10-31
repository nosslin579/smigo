package kga;

import kga.rules.CompanionRule;
import kga.rules.RepetitionRule;
import kga.rules.Rule;
import kga.rules.RuleType;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

public class GardenTest {

    public static final String HINT_MESSAGE_KEY = "hint.hint";
    private Family mockFamily = new Family(1, "Mock Family");
    private Species carrot = Species.create(1, mockFamily, true, false);
    private Species onion = Species.create(2, mockFamily, true, false);
    private Species item = Species.create(3, mockFamily, false, true);
    private Rule twoBrother = new CompanionRule(1, carrot, RuleType.GOODCOMPANION, onion, HINT_MESSAGE_KEY);
    private Rule repetition = new RepetitionRule(2, carrot, RuleType.SPECIESREPETITION, 4, HINT_MESSAGE_KEY);
    private Garden g;
    private Location l = new YearXY(2000, 0, 0);

    @BeforeMethod
    public void setUp() throws Exception {
        g = new Garden();
        g.addOrGetSquare(l).addSpecies(carrot);
        g.addOrGetSquare(new YearXY(2000, 1, 0)).addSpecies(item);
        carrot.addRule(twoBrother);
        carrot.addRule(repetition);
    }

    @Test
    public void companionRuleShouldReturnHint() throws Exception {
        g.addOrGetSquare(l).addSpecies(onion);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 1);
        Assert.assertEquals(hints.iterator().next().getMessageKey(), HINT_MESSAGE_KEY);
    }

    @Test
    public void companionRuleShouldNotReturnHint() throws Exception {
        g.addOrGetSquare(new YearXY(2000, 0, 2)).addSpecies(onion);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 0);
    }

    @Test
    public void repetitionRuleShouldReturnHint() throws Exception {
        g.addOrGetSquare(new YearXY(2001, 0, 0)).addSpecies(carrot);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 1);
        Assert.assertEquals(hints.iterator().next().getMessageKey(), HINT_MESSAGE_KEY);
    }

    @Test
    public void repetitionRuleShouldNotReturnHint() throws Exception {
        g.addOrGetSquare(new YearXY(2007, 0, 0)).addSpecies(carrot);
        List<Hint> hints = g.getHints();
        Assert.assertEquals(hints.size(), 0);
    }

    @Test
    public void getSquares() throws Exception {
        Garden garden = new Garden();
        garden.addOrGetSquare(l).addSpecies(carrot);
        garden.addOrGetSquare(l).addSpecies(onion);
        Assert.assertEquals(garden.getSquare(l).getPlants().size(), 2);
        Map<Integer, Collection<Square>> squares = garden.getSquares();
        Assert.assertEquals(squares.get(2000).size(), 1);
        Assert.assertEquals(squares.get(2000).iterator().next().getPlants().size(), 2);
    }

    @Test
    public void addYear() throws Exception {
        g.addYear(2001);
        Collection<Square> squaresFor = g.getSquares().get(2001);
        Assert.assertEquals(squaresFor.size(), 1);
        Assert.assertEquals(squaresFor.iterator().next().getLocation(), new YearXY(2001, 1, 0));
    }
}