<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>

<jsp:include page="header.jsp"/>

<style>
    #mainmenu .mainmenuitem:nth-of-type(1) {
        background: #466919 !important;
    }
</style>

<script type="text/javascript" charset="utf-8">
var speciesList = {},
        selectedSpecies, //current selected species
        origo,//anonymous class with absolute x and y, needs update when expand or change year
        ABSOLUTE_COORDS = true,//constant
        RELATIVE_COORDS = false,//constant
        touchDevice = 'ontouchstart' in document.documentElement,
        unsavedChanges = false;
/**
 * Converts relative coordinate to absolute coordinate
 */
function getAbsolute(relative) {
    return {x: origo.x + relative.x, y: origo.y + relative.y};
}

/**
 * Converts absolute coordinate to relative coordinate
 */
function getRelative(absolute) {
    return {x: absolute.x - origo.x, y: absolute.y - origo.y};
}

/**
 * Constructor for the Species object
 * @param id the id of the species
 * @param name the scientific name
 * @param family
 * @param annual annual or perennial
 * @param isItem item or plant
 * @param translatedname
 * @param iconfilename
 */
function Species(id, name, isItem, translatedname, iconfilename) {
    this.name = name;
    this.id = id;
    this.isItem = isItem;
    this.translatedname = translatedname;
    this.iconfilename = iconfilename;
}

clickNoMove = {
    name: 'clicknomove',
    moved: false,
    handle: function(ev) {
        switch(ev.type) {
            case 'touchstart':
                clickNoMove.moved = false;
                break;
            case 'touchmove':
                clickNoMove.moved = true;
                break;
            case 'touchend':
                if (!clickNoMove.moved) {
                    ev.target.dispatchEvent(new Event(clickNoMove.name));
                }
                clickNoMove.moved = false;
                break;
        }
    }
};

//On load
$(function () {
    window.onbeforeunload = function() {
        if(unsavedChanges) {
            return '<spring:message code="msg.unsaved-changes"/>';
        }
    }
    if (document.referrer && document.referrer.length > 0 && document.referrer.indexOf('smigo.org') === -1) {
        $.ajax({
            url: '${pageContext.request.contextPath}/referrer',
            type: 'POST',
            data: document.referrer,
    //        dataType: 'text/plain',
            contentType: 'text/plain'
        })
    }

    <c:forEach items="${specieslist}" var="sp">
    speciesList[${sp.id}] = new Species(${sp.id}, "${sp.scientificName}", ${sp.item}, '${sp.translation}', '${sp.iconFileName}');
    </c:forEach>
    smigolog("speciesList", speciesList);

    selectedSpecies = speciesList[28];

// garden.addPlant(new Plant(specieslist[11],2011,1,1));

    $('.jsgridcell').on('touchstart touchmove touchend',clickNoMove.handle);

    if ($('.jsgridcell').size() > 3) {
        origo = $('#origo').coordinates(ABSOLUTE_COORDS);
        if (touchDevice) {
            smigolog('Touch device is true',touchDevice);
            $('.selectspeciesitem').on('touchstart', function (e) {
                $('#debug').text('tap, .selectspeciesitem');
                $('.selectspeciestooltipcontainer').hide();
                $(this).siblings().show().delay(5000).fadeOut();
                setSelectedSpecies.call(this);
            });
            var $selectspeciestooltipcontainer = $('.selectspeciestooltipcontainer');
            $selectspeciestooltipcontainer.on('touchstart', function () {
                $('#debug').text('hide tooltip');
                $selectspeciestooltipcontainer.hide();
            });
            $('.jsgridcell').on('clicknomove', addPlant);
        } else {
            smigolog('Touch device is false',touchDevice);
            $('.jsgridcell').click(addPlant);//for adding species
            $('.selectspeciesitem').click(setSelectedSpecies);//for selecting species
        }
//        document.getElementById('origo').dispatchEvent(new Event('clicknomove'));
    }

    $('#savebutton').on("click", save);

// for(key in zxcv) {debug('zxcv['+key+']: ' + zxcv[key]);}

});

/**
 * Ajax call to save garden
 */
function save() {
    $('#savebutton').unbind("click");
    $('#userdialog').text('<spring:message code="general.saving"/>');
    var jsonPlants = new Array();
    jsonPlants.push({year:${year}});
    $('.speciesimage').each(function (index, speciesImage) {
        var speciesId = parseInt($(speciesImage).speciesid());
        var coord = $(speciesImage).parents('.jsgridcell').coordinates();
        jsonPlants.push({a: speciesId, x: +coord.x, y: +coord.y});
    });

    var jsonPlantsStringified = JSON.stringify(jsonPlants);
    smigolog("Sending json", jsonPlants);
    $.ajax({
        url: '${pageContext.request.contextPath}/savegarden',
        dataType: 'html',
//contentType: 'application/json; charset=utf-8',
        type: 'POST',
        data: ({jsonstringyearandplants: jsonPlantsStringified}),
        cache: false,
        ifModified: true,
        success: function (data) {
            $('#userdialog').text(data);
            smigolog('data recieved: ', data);
        },
        error: function (jqXHR, textStatus, errorThrown) {
            $('#userdialog').text('Error');
            smigolog("ajax error> jqXHR=" + jqXHR + " textStatus=" + textStatus + " errorThrown=" + errorThrown);
        },
        complete: function (jqXHR, textStatus) {
            $('#savebutton').on("click", save);
            unsavedChanges = false;
        }
    });

}


/**
 * Adds or removes a plant to the grid
 * @param e the event object
 */
function addPlant(e) {
    unsavedChanges = true;
    var $this = $(this);
    var coord = $this.coordinates();//relative coordinates
    smigolog("Adding or removing plant to coord", coord);
    if (e.shiftKey || selectedSpecies.deleteSquare) {
        $this.removePlantImage();
    } else if (selectedSpecies.viewSquare) {
        $this.find('.gridsquaretooltip').show().delay(5000).fadeOut();
    } else {
        var species = $this.getspecies();
        for (sp in species) {
            if (species[sp].id == selectedSpecies.id ||
                    species[sp].isItem || selectedSpecies.isItem) {
                alert('Cant add plant there!');
                return;
            }
        }
        species.push(selectedSpecies);
        expandGrid(coord);
        $this.addPlantImage(species);
    }
}

/**
 * Expands grid until the relativeCoord is visible with one square marginal.
 * @returns absolute coordinates.
 */
function expandGrid(relativeCoord) {
    smigolog("Expanding grid", relativeCoord);
    var needsMoreExpandcheck = true;
    while (needsMoreExpandcheck) {
//alert('Expanding ' + relativeCoord.x + ', ' + relativeCoord.y);
        var absolute = getAbsolute(relativeCoord);
        if (absolute.y <= 0) {
            $('.gridrow:first').clone(true).insertBefore('.gridrow:first');
        } else if (absolute.y >= $('.jsgridcell:last').coordinates(ABSOLUTE_COORDS).y) {
            $('.gridrow:last').clone(true).insertAfter('.gridrow:last');
        } else if (absolute.x <= 0) {
            $('.jsgridcell:first-child').before($('.jsgridcell:first').clone(true));
        } else if (absolute.x >= $('.jsgridcell:last').coordinates(ABSOLUTE_COORDS).x) {
            $('.jsgridcell:last-child').after($('.jsgridcell:first').clone(true));
        } else {
            needsMoreExpandcheck = false;
        }
        origo = $('#origo').coordinates(ABSOLUTE_COORDS);
    }
    return absolute;
}


/**
 * Adds a image to the gridsquare
 * @param species an array of species object
 */
(function ($) {
    $.fn.addPlantImage = function (species) {
//this refers to the div with class squarecontent inside a cell
        smigolog('Adding image at ' + this.coordinates().x + ':' + this.coordinates().x + ' to ' + this.length + ' gridsquares: ', this);
        var squareHTML = '';
        var imgclass = species.length == 1 ? "speciesimage" : "speciesimage smallspeciesimage";
        for (sp in species)
            squareHTML += '<img id="jsGridSpeciesId_' + species[sp].id + '" src="/pic/' + species[sp].iconfilename + '" class="' + imgclass + '"/>';
        this[0].innerHTML = squareHTML;

    }
})(jQuery);


/**
 * Removes all images from the gridsquare
 *
 */
(function ($) {
    $.fn.removePlantImage = function (plant) {
//this refers to the div with class squarecontent inside a cell
        smigolog("Removing all plants", this);
        this.empty().siblings('.gridsquaretooltip').empty();
    }
})(jQuery);

/**
 * Sets the selected species.
 */
function setSelectedSpecies() {
//    alert(this);
    var speciesId = $(this).parent().attr('id').replace('jslistspeciesid_', "");
    if (speciesId == 'deleteSquare') {
        selectedSpecies = {deleteSquare: true}
    } else if (speciesId == 'viewSquare') {
        selectedSpecies = {viewSquare: true}
    } else {
        selectedSpecies = speciesList[+speciesId];
        smigolog('Selected species set to:' + selectedSpecies.id + ', name:' + selectedSpecies.name);
    }
    $('.selected').removeClass('selected');
    $('#jslistspeciesid_' + speciesId).addClass('selected');
}

/**
 * Returns either relative or absolute coordinates of gridcell
 * @param abs if true returns absolute, otherwise returns relative
 */
(function ($) {
    $.fn.coordinates = function (abs) {
//smigolog('Coordinates, retrieving from ' + this.get(0));
        if (this.is('.jsgridcell')) {
            var absolute = {
                x: this.prevAll().size(),
                y: this.parents('.gridrow').prevAll().size()
            };
        } else {
            var absolute = {
                x: this.parents('.jsgridcell').prevAll().size(),
                y: this.parents('.gridrow').prevAll().size()
            };
        }
        if (abs)
            return absolute;
        smigolog('Returning relative coordinates', getRelative(absolute));
//else return relative coordinates
        return getRelative(absolute);
    };
})(jQuery);


/**
 * Extracts the species id from the speciesimage. 293486
 */
(function ($) {
    $.fn.speciesid = function () {
        smigolog("Extract species id from image", this.attr("id"));
        var idArray = this.attr("id").split("_");
        if (idArray[0] != "jsGridSpeciesId")
            throw "No jsGridSpeciesId found on " + this.parent().html();
        return idArray[1];
    };
})(jQuery);


/**
 * Extracts the species ids form the gridsquare
 */
(function ($) {
    $.fn.getspecies = function () {
        smigolog("Extracting all species id from ", this);
        var ret = new Array();
        this.find('.speciesimage').each(function (index, imgElement) {
            var speciesId = $(imgElement).speciesid();
            ret.push(speciesList[speciesId]);
        });
        return ret;
    };
})(jQuery);

</script>


<div id="gardenpage">

    <!-- ################ a list of species, selectable for adding to grid #################-->
    <div id="selectspecieslist" class="smigoframe">
        <div id="selectspecieslistheader" class="smigoframeheader"><spring:message code="plants"/></div>
        <div class="smigoframecontent">

            <%--view square--%>
            <div id="jslistspeciesid_viewSquare" class="selectspeciescontainer">
                <div class="selectspeciesitem">
                    <spring:message code="msg.view-square.title"/>
                </div>
                <div class="selectspeciestooltipcontainer smigoframe hide">
                    <div class="smigoframecontent">
                        <spring:message code="msg.view-square.description"/>
                    </div>
                </div>
            </div>

            <%--delete square--%>
            <div id="jslistspeciesid_deleteSquare" class="selectspeciescontainer">
                <div class="selectspeciesitem">
                    <spring:message code="deletesquare"/>
                </div>
                <div class="selectspeciestooltipcontainer smigoframe hide">
                    <div class="smigoframecontent">
                        <spring:message code="msg.deletesquare.description"/>
                    </div>
                </div>
            </div>

            <hr/>

            <c:forEach items="${specieslist}" var="currentspecies">
                <div id="jslistspeciesid_${currentspecies.id}" class="selectspeciescontainer <c:if test="${currentspecies.item}"> item</c:if> <c:if test="${currentspecies.annual}"> annual</c:if>">
                    <div class="selectspeciesitem">
                            ${currentspecies.translation}
                    </div>

                    <div class="selectspeciestooltipcontainer smigoframe hide">
                        <div class="speciestranslation smigoframeheader">${currentspecies.translation}</div>
                        <div class="smigoframecontent">
                            <div class="speciesscientificname">${currentspecies.scientificName}</div>
                            <div class="speciesicon"><img src="/pic/${currentspecies.iconFileName}"/></div>

                            <c:if test="${!currentspecies.item}">
                                <div class="familyheader"><spring:message code="family"/></div>
                                <div class="familyitem"><spring:message code="${currentspecies.family.translationKey}"/></div>
                                <div class="typeheader"><spring:message code="type"/></div>
                                <div class="typeitem"><spring:message code="${currentspecies.annual  ? 'annual' : 'perennial'}"/></div>

                                <c:if test="${!empty currentspecies.companionPlantingRules}">
                                    <div class="companionplantingheader ruleheader"><spring:message code="companionplanting"/></div>
                                    <c:forEach var="currentRule" items="${currentspecies.companionPlantingRules}">
                                        <div class="companionplantinghint ruleitem"><c:out value="${currentRule.info}"/></div>
                                    </c:forEach> <!-- end hint -->
                                </c:if>

                                <c:if test="${!empty currentspecies.cropRotationRules}">
                                    <div class="croprotationheader ruleheader"><spring:message code="croprotation"/></div>
                                    <c:forEach var="currentRule" items="${currentspecies.cropRotationRules}">
                                        <div class="croprotationhint ruleitem"><c:out value="${currentRule.info}"/></div>
                                    </c:forEach> <!-- end hint -->
                                </c:if>


                            </c:if>

                        </div>
                    </div>
                    <!-- end tooltip -->

                </div>
            </c:forEach>
        </div>
    </div>


    <!-- ################ garden menu #################-->
    <div id="gardenmenu" class="floatcontainer">
        <!-- ################ user messages #################-->
        <div class="smigoframe left">
            <div class="smigoframeheader">Smigo</div>
            <div class="smigoframecontent">
                <div id="userdialog"><spring:message code="garden.dontforgettosave"/></div>
            </div>
        </div>


        <!-- ################ garden actions #################-->
        <div id="gardenactions" class="left smigoframe">
            <div class="smigoframeheader">
                <spring:message code="general.menu"/>
            </div>
            <div class="iconbuttons smigoframecontent">
                <a id="savebutton" class="iconbutton">
                    <img src="/static/icon/save.png" title="<spring:message code="save"/>"></a>
                <a id="add-year-link" href="${pageContext.request.contextPath}/addyear" class="iconbutton">
                    <img src="/static/icon/addyear.png" title="<spring:message code="addyear"/>"></a>
                <a href="${pageContext.request.contextPath}/deleteyear" class="iconbutton">
                    <img src="/static/icon/deleteyear.png" title="<spring:message code="deleteyear"/>"></a>
                <a href="${pageContext.request.contextPath}/add-species" class="iconbutton">
                    <img src="/static/icon/addspecies.png" title="<spring:message code="addspecies"/>"></a>
            </div>
        </div>


        <!-- ################ links to available years #################-->
        <div id="gardenyears" class="left smigoframe">
            <div class="smigoframeheader">
                <spring:message code="year"/>
            </div>
            <div class="smigoframecontent selectyears">
                <c:forEach items="${kgagarden.years}" var="currentyear">
                    <a href="${pageContext.request.contextPath}/garden/${currentyear}" class="selectyear<c:if test="${currentyear == year}">
                currentyear</c:if>">${currentyear}</a>
                </c:forEach>
            </div>
        </div>


    </div>
    <!-- gardenmenu div-->


    <!-- ################ thegrid #################-->
    <div id="thegrid">
        <div class="smigoframe">
            <c:forEach var="squareiterator" items="${kgagarden.iterator}">
                <c:if test="${squareiterator.year eq year}">
                    <c:forEach var="currentSquare" items="${squareiterator}">

                        <c:if test="${squareiterator.startOfRow}">
                            <div class="gridrow">
                        </c:if>

                        <div class="gridsquarecontainer jsgridcell"
                             <c:if test="${currentSquare.origo}">id="origo"</c:if>>
                            <!-- ${currentSquare.year} ${currentSquare.x} ${currentSquare.y} ${currentSquare.size} ${bounds.minx} ${bounds}  -->

                            <div class="gridsquaresquare">
                                <c:set var="imgclass" value="${currentSquare.size > 1 ? 'speciesimage smallspeciesimage':'speciesimage'}"/>
                                <c:forEach var="currentPlant" items="${currentSquare.plants}">
                                    <img id="jsGridSpeciesId_${currentPlant.species.id}" src="/pic/${currentPlant.species.iconFileName}"
                                         class="${imgclass}"/>
                                </c:forEach>
                            </div>

                            <div class="gridsquaretooltip smigoframe">
                                <c:forEach var="currentPlant" items="${currentSquare.plants}">
                                    <div class="gridsquaretooltipitem">
                                        <div class="translation"><c:out value="${currentPlant.species.translation}"/></div>
                                        <div class="scientificname"><c:out value="${currentPlant.species.scientificName}"/></div>
                                        <div class="speciesicon"><img src="/pic/${currentPlant.species.iconFileName}"/></div>

                                        <c:if test="${!empty currentPlant.companionPlantingHints}">
                                            <div class="companionplantingheader hintheader"><spring:message code="companionplanting"/></div>
                                            <c:forEach var="currentCompanionPlantingHint" items="${currentPlant.companionPlantingHints}">
                                                <div class="companionplantinghint hint"><c:out
                                                        value="${currentCompanionPlantingHint.rule.info}"/></div>
                                            </c:forEach> <!-- end hint -->
                                        </c:if>

                                        <c:if test="${!empty currentPlant.cropRotationHints}">
                                            <div class="croprotationheader hintheader"><spring:message code="croprotation"/></div>
                                            <c:forEach var="currentCropRotationHint" items="${currentPlant.cropRotationHints}">
                                                <div class="croprotationhint hint"><c:out value="${currentCropRotationHint.rule.info}"/></div>
                                            </c:forEach> <!-- end hint -->
                                        </c:if>

                                    </div>
                                </c:forEach> <!-- end plants -->
                            </div>
                            <!-- end gridsquaretooltip -->

                        </div>
                        <c:if test="${squareiterator.endOfRow}">
                            </div>
                            <!-- end gridrow -->
                        </c:if>
                    </c:forEach><!-- end squares -->
                </c:if><!-- end if currentyear -->
            </c:forEach>
        </div>
        <!-- end smigoframe -->
    </div>
    <!-- end gridtable -->
</div>
<!-- gardenpage -->
</div><!-- gardenpage -->

<jsp:include page="footer.jsp"/>