<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:include page="../../header.jsp"/>
<jsp:include page="nav.jsp"/>

<div ng-view="" class="angular-view ng-scope">
    <div class="container ng-scope">

        <div class="row">
            <div class="col-sm-offset-2 col-sm-8">
                <div class="page-header text-center">
                    <h1 class="ng-binding">
                        Forum
                    </h1>
                </div>

                <!-- ngIf: user.currentUser -->
                <hr>

                <!-- ngRepeat: m in messages -->
                <div ng-repeat="m in messages" class="ng-scope">
                    <div class="ng-binding">
                        <a href="/wall/frango" class="ng-binding">frango</a> 2015-01-12
                    </div>
                    <div class="ng-binding">
                        Share your garden plan here. Click username to view plan.
                    </div>
                    <hr>

                </div>
                <!-- end ngRepeat: m in messages -->
                <!-- ngIf: hasMoreMessages -->
                <!-- ngIf: pendingGetMore -->
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