<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:include page="../../header.jsp"/>
<jsp:include page="nav.jsp"/>

<div ng-view="" class="angular-view ng-scope">
    <div class="container ng-scope">
        <div class="page-header text-center">
            <h1 class="ng-binding">Logga in</h1>
        </div>

        <div class="row">
            <div class="col-sm-5">
                <h3 class="ng-binding">Använd ett externt konto</h3>

                <div>
                    <a onclick="$('#facebookSignin').submit()" href="" class="list-group-item"><span class="social-sprite facebook" style="margin-right: 10px;"></span>Facebook</a>
                    <a onclick="$('#yahooOpenId').submit()" href="" class="list-group-item"><span class="social-sprite yahoo" style="margin-right: 10px;"></span>Yahoo</a>
                    <a ng-click="requestFeature('google-sign-in')" href="" class="list-group-item"><span class="social-sprite google" style="margin-right: 10px;"></span>Google</a>
                    <a ng-click="requestFeature('twitter-sign-in')" href="" class="list-group-item"><span class="social-sprite twitter" style="margin-right: 10px;"></span>Twitter</a>
                </div>

                <form name="fb_signin" id="facebookSignin" action="/auth/facebook" method="POST" class="ng-pristine ng-valid">
                    <input type="hidden" name="scope" value="public_profile,email">
                </form>

                <form action="login-openid" id="googleOpenId" method="post" class="ng-pristine ng-valid">
                    <input type="hidden" name="openid_identifier" value="https://www.google.com/accounts/o8/id">
                    <input type="hidden" name="remember-me" value="true">
                </form>

                <form action="login-openid" id="yahooOpenId" method="post" class="ng-pristine ng-valid">
                    <input type="hidden" name="openid_identifier" value="https://me.yahoo.com">
                    <input type="hidden" name="remember-me" value="true">
                </form>

                <hr>

                <form action="login-openid" method="POST" role="form" class="ng-pristine ng-valid">
                    <input type="hidden" name="remember-me" value="true">

                    <div class="form-group">
                        <label for="openid-url">Open Id URL</label>
                        <input required="" placeholder="https://openid.net/" class="form-control" id="openid-url" type="text" size="30" name="openid_identifier">
                    </div>
                    <div class="form-group">
                        <button name="submit" type="submit" class="btn btn-default ng-binding">Logga in</button>
                    </div>
                </form>
            </div>

            <div class="col-sm-5 col-sm-offset-2">
                <h3 class="ng-binding">Använd ett Smigo konto</h3>

                <form name="loginOrRegisterform" role="form" novalidate="" ng-submit="submitLoginOrRegisterForm(loginOrRegisterform,formModel,widgetId)" class="ng-pristine ng-valid ng-valid-required ng-valid-pattern ng-valid-minlength ng-valid-parse ng-valid-maxlength">
                    <div class="has-error">
                        <!-- ngRepeat: error in loginOrRegisterform.objectErrors -->
                    </div>

                    <div class="form-group" ng-class="{'has-error':loginOrRegisterform.username.$invalid &amp;&amp; (loginOrRegisterform.username.$dirty || loginOrRegisterform.submitted)}">
                        <label for="username" class="ng-binding">Användarnamn</label>
                        <input type="text" class="form-control ng-pristine ng-untouched ng-valid ng-valid-required ng-valid-pattern ng-valid-minlength ng-valid-parse ng-valid-maxlength" id="username" name="username" placeholder="Användarnamn" ng-model="formModel.username" ng-required="true" ng-minlength="0" ng-maxlength="999" ng-pattern="/.+/" required="required">

                        <!-- ngIf: loginOrRegisterform.username.$dirty || loginOrRegisterform.submitted -->
                    </div>

                    <div class="form-group" ng-class="{'has-error':loginOrRegisterform.password.$invalid &amp;&amp; (loginOrRegisterform.password.$dirty || loginOrRegisterform.submitted)}">
                        <label for="password" class="ng-binding">Lösenord</label>
                        <input type="password" class="form-control ng-pristine ng-untouched ng-valid ng-valid-required ng-valid-minlength ng-valid-parse" id="password" name="password" placeholder="Lösenord" ng-model="formModel.password" ng-required="true" ng-minlength="0" required="required">

                        <!-- ngIf: loginOrRegisterform.password.$dirty || loginOrRegisterform.submitted -->
                    </div>

                    <!-- ngIf: viewModel.register -->

                    <!-- ngIf: viewModel.register -->

                    <!-- ngIf: viewModel.register -->

                    <!-- ngIf: viewModel.login -->
                    <div class="form-group checkbox ng-scope" ng-if="viewModel.login">
                        <label for="remember-me" class="checkbox ng-binding">
                            <input type="checkbox" id="remember-me" name="remember-me" ng-model="formModel['remember-me']" class="ng-pristine ng-untouched ng-valid">
                            Kom ihåg mig
                        </label>
                    </div>
                    <!-- end ngIf: viewModel.login -->

                    <button type="submit" class="btn btn-default ng-binding" ng-class="{disabled: loginOrRegisterform.processing}" id="submit-login-register-form">Logga in</button>
                    <!-- ngIf: loginOrRegisterform.processing -->
                </form>

                <!-- ngIf: viewModel.login -->
                <div ng-if="viewModel.login" class="ng-scope">
                    <a id="request-password-link" class="pull-right ng-binding" href="/request-password-link">Glömt lösenord?</a>
                </div>
                <!-- end ngIf: viewModel.login -->

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