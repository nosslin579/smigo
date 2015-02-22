<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:include page="../../header.jsp"/>
<jsp:include page="nav.jsp"/>
<div ng-view="" class="angular-view ng-scope">
    <div class="container ng-scope">
        <div class="page-header text-center">
            <h1 class="ng-binding">
                Vanlig frågor
            </h1>
        </div>

        <h3 class="ng-binding">Vad är detta?</h3>
        <p class="ng-binding">-Smigo är en trädgårdsplannerare, inspirerad av "Odla i kvadrat". Du kan plannera din grönsakodling och få tips om samodling och växelbruk samt dela din planering på social medier.</p>

        <h3 class="ng-binding">Hur funkar det?</h3>
        <p class="ng-binding">
            1. Välj en planta i vänsterkolumnen.<br>
            2. Klick någonstans i rutnätet. Nu har du lagt till din första ruta.<br>
            3. Lägg till fler plantor tills rutnätet motsvarar din trädgård.<br>
            4. Registrera dig på sajten för att spara och dela din plannering på t.ex. facebook.<br>
            <a href="/wall/example" class="ng-binding">Visa ett exempel</a>
        </p>

        <h3 class="ng-binding">Hur tar jag bort en plant från rutnätet?</h3>
        <p class="ng-binding">-Håll nere shift och klicka i rutan eller välj "Radera ruta" i övre vänstra hörnet.</p>

        <h3 class="ng-binding">Är det gratis?</h3>
        <p class="ng-binding">-Ja det är gratis.</p>

        <h3 class="ng-binding">Min fråga finns inte här. Vad göra?</h3>
        <p class="ng-binding">-Lämna ett meddelande:
            <a class="text-lowercase ng-binding" href="/forum">Forum</a> eller kontakta mig:</p>

        <div class="media">
            <a class="pull-left" href="mailto:christian1195@gmail.com" target="_blank"><span class="social-sprite email"></span></a>
            <a class="pull-left" href="https://www.facebook.com/smigogarden" target="_blank"><span class="social-sprite facebook"></span></a>
            <a class="pull-left" href="https://www.twitter.com/smigogarden" target="_blank"><span class="social-sprite twitter"></span></a>
            <a class="pull-left" href="http://se.linkedin.com/pub/christian-nilsson/3b/798/a5b/" target="_blank"><span class="social-sprite linkedin"></span></a>
            <a class="pull-left" href="http://www.reddit.com/r/smigo" target="_blank"><span class="social-sprite reddit"></span></a>
        </div>

        <div class="page-header text-center">
            <h1>
                TODO - Suggested features
            </h1>
        </div>

        <ul>
            <li>Add reference to companion rules and disable invalid rules or completly remove the hint system.</li>
            <li>Improve support for small devices.</li>
            <li>Create more beautiful icons to achive a uniform look.</li>
            <li>Your suggestion is welcome...</li>
        </ul>

        <div class="page-header text-center">
            <h1 class="ng-binding">
                Kända buggar
            </h1>
        </div>

        <ul>
            <li>Old versions of Firefox displays the grid out of sync with icons.</li>
            <li>Adding several years without adding plants results in no grid.</li>
        </ul>

        <p class="text-center">
            <a target="_blank" href="/static/version-history.html">Version history</a>
        </p>

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