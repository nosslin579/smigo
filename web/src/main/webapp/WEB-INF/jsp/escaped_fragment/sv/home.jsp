<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:include page="../../header.jsp"/>
<jsp:include page="nav.jsp"/>

<div ng-view="" class="angular-view ng-scope">
    <div class="jumbotron ng-scope" style="text-align: center;">
        <div class="container">

            <h1 class="ng-binding">Trädgårdsplanering</h1>
            <p class="ng-binding">
                Smigo är ett gratis och enkelt program för att planera din köksträdgård och som hjälper dig med samodling och växelbruk. Webbaserat, öppen källkod och lätt att använda. Stödjer alla operativsystem inklusive iOS, iPhone, iPad, Android, Windows och Mac.
            </p>
            <p><a class="btn btn-primary btn-lg ng-binding" href="/garden" role="button">Prova nu</a></p>
        </div>
    </div>
    <div class="container ng-scope">
        <div class="row">
            <div class="col-md-4">
                <h2 class="ng-binding">Online</h2>
                <p class="ng-binding">Du behöver inte ladda ner eller installera något. Smigo är en webbapplikation och du använder din webbläsare. Det enda du behöver är en dator eller surfplatta med internetuppkoppling.</p>
            </div>
            <div class="col-md-4">
                <h2 class="ng-binding">Planering</h2>
                <p class="ng-binding">Rita en layout av din grönsakodling. Välj mellan hundratals växter i våran databas eller lägg till egna med ett enkelt klick. Rutnätet expanderar allteftersom du lägger till plantor. Smigo funkar utmärkt med "Odla i kvadrat" (square foot gardening).</p>
            </div>
            <div class="col-md-4">
                <img ng-src="/static/icon/snapshot.png" alt="vegetable garden layout" class="img-rounded full-width" src="/static/icon/snapshot.png">
            </div>
        </div>

        <div class="row">
            <div class="col-md-4 hidden-xs hidden-sm">
                <img ng-src="/static/icon/kitchengarden.jpg" alt="kitchen garden" class="img-rounded full-width" src="/static/icon/kitchengarden.jpg">
            </div>
            <div class="col-md-8">
                <h2 class="ng-binding">Gratis</h2>
                <p class="ng-binding">Gratis och med öppen källkod så du kan följa och bidra till utvecklingen av programmet. Koden finns på
                    <a href="https://github.com/nosslin579/smigo">Github</a></p>
                <p class="ng-binding">Tillgänglig på flera andra språk så som engelska, spanska och tyska.</p>
            </div>
        </div>
        <!-- ngInclude: 'footer.html' -->
        <div ng-include="'footer.html'" class="ng-scope">
            <hr style="border-top-color:#B8B8B8;margin-top: 100px" class="ng-scope">

            <div class="row ng-scope">
                <div class="col-sm-offset-1 col-sm-10 col-md-offset-2 col-md-8">
                    <footer>
                        <div class="row">
                            <div class="col-sm-offset-1 col-xs-4">
                                <div style="text-decoration: underline;" class="ng-binding">Om</div>
                                <a href="https://github.com/nosslin579/smigo" class="ng-binding">Källkod</a><br>
                                <a href="/static/terms-of-service.html" rel="nofollow" class="ng-binding">Användaravtal</a><br>
                                <a href="/help" class="ng-binding">Hjälp</a><br>
                                <a href="/forum" class="ng-binding">Forum</a><br>
                            </div>
                            <div class="col-xs-4">
                                <div style="text-decoration: underline;" class="ng-binding">Kontakt</div>
                                <a href="http://www.reddit.com/r/smigo" target="_blank">Reddit</a><br>
                                <a href="https://www.facebook.com/smigogarden" target="_blank">Facebook</a><br>
                                <a href="https://www.twitter.com/smigogarden" target="_blank">Twitter</a><br>
                                <a href="http://se.linkedin.com/pub/christian-nilsson/3b/798/a5b/" target="_blank">Linkedin</a><br>
                            </div>
                            <div class="col-xs-4 col-xs-3">
                                <div style="text-decoration: underline;" class="ng-binding">Länkar</div>
                                <a href="http://stud.epsilon.slu.se/5256/" target="_blank" class="ng-binding">Samodling</a><br>
                                <a href="http://sv.wikipedia.org/wiki/V%C3%A4xelbruk" target="_blank" class="ng-binding">Växelbruk</a><br>
                                <a href="http://sourceforge.net/projects/kitchengarden" target="_blank" class="ng-binding">Kitchen garden aid</a><br>
                                <a href="http://sv.wikipedia.org/wiki/Gr%C3%B6nsaksland" target="_blank" class="ng-binding">Grönsaksland</a><br>
                            </div>
                        </div>
                        <div class="row" style="margin: 16px;">
                            <div class="text-center">Copyright (C) 2011-2015 Christian Nilsson - christian1195@gmail.com</div>
                        </div>
                    </footer>
                </div>
            </div>
        </div>
    </div>
</div>

<jsp:include page="../footer.jsp"/>