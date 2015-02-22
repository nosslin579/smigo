<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:include page="../header.jsp"/>
<jsp:include page="nav.jsp"/>
<div ng-view="" class="angular-view ng-scope">
    <div class="container-fluid full-height ng-scope">
        <div class="row full-height">
            <div species-tooltip="" class="species-tooltip hidden-xs ng-isolate-scope" style="visibility: hidden;">
                <div class="popover right in fade show">
                    <div class="popover-inner">
                        <div class="popover-title text-center">
                            <h4>
                                <img class="small-species" ng-src="/static/species/116.png" src="/static/species/116.png">
                                <a href="/species/116"><strong style="padding-right: 20px;" class="ng-binding">Path</strong></a>
                                <button type="button" class="close" style="color: #000;padding: 0px 10px 10px 10px;">
                                    <span>×</span>
                                </button>
                            </h4>

                        </div>

                        <!-- ngIf: species.item -->
                        <div class="popover-content ng-binding ng-scope" ng-if="species.item">
                            This is an item. Use it as location reference in your planning.
                        </div>
                        <!-- end ngIf: species.item -->
                        <!-- ngIf: !species.item -->
                    </div>
                </div>
            </div>
            <div id="species-frame" class="col-xs-3 full-height">
                <div class="species-wrapper">
                    <div class="btn-group btn-group-justified">
                        <div class="btn-group">
                            <button type="button" class="btn btn-block btn-default" ng-class="{'btn-info': speciesState.action == 'info'}" ng-click="speciesState.action = 'info'" title="View" tooltip="View" tooltip-placement="bottom" tooltip-trigger="click">
                                <span class="glyphicon glyphicon-info-sign"></span><span class="visible-lg-inline ng-binding"> View</span>
                            </button>
                        </div>
                        <div class="btn-group">
                            <button type="button" class="btn btn-block btn-default btn-success" ng-class="{'btn-success': speciesState.action == 'add'}" ng-click="speciesState.action = 'add'" title="Add" tooltip="Add" tooltip-placement="bottom" tooltip-trigger="click">
                                <span class="glyphicon glyphicon-plus-sign"></span><span class="visible-lg-inline ng-binding"> Add</span>
                            </button>
                        </div>
                        <div class="btn-group">
                            <button type="button" class="btn btn-block btn-default" ng-class="{'btn-danger': speciesState.action == 'delete'}" ng-click="speciesState.action = 'delete'" title="Delete" tooltip="Delete" tooltip-placement="bottom" tooltip-trigger="click">
                                <span class="glyphicon glyphicon-remove-sign"></span><span class="visible-lg-inline ng-binding"> Delete</span>
                            </button>
                        </div>
                    </div>
                    <hr>
                    <form class="navbar-form ng-pristine ng-valid" ng-submit="selectedSpeciesFromTopResult(search.query)">
                        <input class="form-control full-width ng-pristine ng-untouched ng-valid" ng-class="{loadinggif: search.proccessing}" type="text" placeholder="Search" ng-model="search.query" ng-change="searchSpecies(search)">
                    </form>
                    <ul class="nav nav-pills nav-stacked">
                        <!-- ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="42">Asparagus</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="48">Basil</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="4">Bean</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="24">Beetroot</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="61">Borage</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="2">Broadbean</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="12">Broccoli</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="13">Brussels sprouts</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="17">Cabbage</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="30">Capsicum</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope active">
                            <a class="select-species-link ng-binding" href="" data-speciesid="28">Carrot</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="11">Cauliflower</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="38">Celeriac</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="37">Celery</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="25">Chard</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="53">Chili</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="54">Chinese cabbage</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="21">Chive</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="118">Clover</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="51">Corn</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="8">Cucumber</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="6">Dill</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="1">Eggplant</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="66">Garlic</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="15">Kohlrabi</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="125">Lacy phacelia</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="100">Lawn</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="20">Leek</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="35">Lettuce</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="103">Marigold</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="26">Melon</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="105">Nasturtium</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="22">Onion</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="10">Onion, Red</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="31">Parsley</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="29">Parsnip</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="116">Path</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="45">Pea</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="109">Pot Marigold</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="47">Potato</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="32">Pumpkin</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="34">Radish</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="36">Rocket</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="43">Spinach</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="108">Squash</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="40">Strawberry</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="73">Sunflower</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="18">Swede</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="44">Tomato</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <li ng-repeat="s in speciesState.speciesArray | speciesFilter:search.query" ng-class="{active: speciesState.selectedSpecies.id === s.id &amp;&amp; speciesState.action == 'add'}" ng-click="selectSpecies(s, $event)" class="ng-scope">
                            <a class="select-species-link ng-binding" href="" data-speciesid="27">Watermelon</a>
                        </li>
                        <!-- end ngRepeat: s in speciesState.speciesArray | speciesFilter:search.query -->
                        <hr>
                        <!-- ngIf: isSpeciesAddable(search.query) && userState.currentUser -->
                        <!-- ngIf: speciesState.pendingAdd -->
                    </ul>
                </div>
            </div>
            <div id="garden-frame" class="col-xs-9 no-select full-height">
                <div class="row">
                    <div class="col-xs-6">
                        <ul class="nav nav-pills" role="tablist">

                            <!--#### Select previous year ####-->
                            <li class="hidden-xs disabled" ng-class="{disabled:!garden.yearSquareMap[garden.selectedYear-1]}">
                                <!-- ngIf: garden.yearSquareMap[garden.selectedYear-1] -->
                                <!-- ngIf: !garden.yearSquareMap[garden.selectedYear-1] --><a ng-if="!garden.yearSquareMap[garden.selectedYear-1]" class="ng-scope">
                                <span class="glyphicon glyphicon-backward" aria-hidden="true"></span>
                            </a><!-- end ngIf: !garden.yearSquareMap[garden.selectedYear-1] -->
                            </li>

                            <!--#### Year selector ####-->
                            <li class="dropdown" dropdown="">
                                <a href="" class="dropdown-toggle ng-binding" dropdown-toggle="" aria-haspopup="true" aria-expanded="false">
                                    2015<span class="caret"></span>
                                </a>
                                <ul class="dropdown-menu">
                                    <!-- ngRepeat: year in garden.getAvailableYears() -->
                                    <li class="select-year ng-scope active" ng-class="{active: year == garden.selectedYear}" ng-repeat="year in garden.getAvailableYears()">
                                        <a href="" ng-click="garden.selectedYear = year" class="ng-binding">
                                            2015
                                        </a>
                                    </li>
                                    <!-- end ngRepeat: year in garden.getAvailableYears() -->
                                    <li class="divider"></li>
                                    <!--#### Add year link 1 ####-->
                                    <li>
                                        <a href="" ng-click="openAddYearModal()" class="ng-binding">
                                            Add year
                                        </a>
                                    </li>
                                </ul>
                            </li>

                            <!--#### Select next year ####-->
                            <li class="hidden-xs disabled" ng-class="{disabled:!garden.yearSquareMap[garden.selectedYear+1]}">
                                <!-- ngIf: garden.yearSquareMap[garden.selectedYear+1] -->
                                <!-- ngIf: !garden.yearSquareMap[garden.selectedYear+1] --><a ng-if="!garden.yearSquareMap[garden.selectedYear+1]" class="ng-scope">
                                <span class="glyphicon glyphicon-forward" aria-hidden="true"></span>
                            </a><!-- end ngIf: !garden.yearSquareMap[garden.selectedYear+1] -->
                            </li>

                            <!--#### Add year link 2 ####-->
                            <li class="hidden-xs hidden-sm">
                                <a id="add-year-link" href="" ng-click="openAddYearModal()" class="ng-binding">
                                    <span class="glyphicon glyphicon-plus"></span>Add year
                                </a>
                            </li>
                        </ul>
                    </div>
                    <!-- ngIf: userState.currentUser.username -->
                    <div class="edit-grid-wrapper ng-isolate-scope" so-grid="garden" so-grid-mutable="true">
                        <div class="peephole">
                            <div class="square-container" ng-style="getGridSizeCss(garden)" style="margin-top: -99905px; width: 100815px; height: 100239px; margin-left: -99905px;">
                                <!-- ngRepeat: s in garden.getSquares() -->
                                <div ng-repeat="s in garden.getSquares()" class="square ng-scope ng-isolate-scope" so-square="s" style="top: 100096px; left: 100672px;">
                                    <div class="square-tooltip">
                                        <table class="table table-bordered">
                                            <tbody>
                                            <tr>
                                                <!-- ngRepeat: p in square.plantArray -->
                                            </tr>

                                            <tr>
                                                <!-- ngRepeat: p in square.plantArray -->
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="square-content full-height">
                                        <!-- ngRepeat: p in square.plantArray -->
                                    </div>
                                </div>
                                <!-- end ngRepeat: s in garden.getSquares() -->
                                <div ng-repeat="s in garden.getSquares()" class="square ng-scope ng-isolate-scope" so-square="s" style="top: 100000px; left: 100000px;">
                                    <div class="square-tooltip">
                                        <table class="table table-bordered">
                                            <tbody>
                                            <tr>
                                                <!-- ngRepeat: p in square.plantArray -->
                                            </tr>

                                            <tr>
                                                <!-- ngRepeat: p in square.plantArray -->
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                    <div class="square-content full-height">
                                        <!-- ngRepeat: p in square.plantArray -->
                                    </div>
                                </div>
                                <!-- end ngRepeat: s in garden.getSquares() -->
                                <!-- ngRepeat: s in garden.getTrailingSquares() -->
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<jsp:include page="footer.jsp"/>