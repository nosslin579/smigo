<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:include page="../header.jsp"/>
<jsp:include page="nav.jsp"/>
<div ng-view="" class="angular-view ng-scope">
    <div class="container ng-scope">
        <div class="page-header text-center">
            <h1 class="ng-binding">Register</h1>
        </div>

        <div class="row">
            <div class="col-sm-5">
                <h3 class="ng-binding">Use another account</h3>

                <div>
                    <a ng-click="requestFeature('google-sign-in')" href="" class="list-group-item"><span class="social-sprite google" style="margin-right: 10px;"></span>Google</a>
                    <a onclick="$('#facebookSignin').submit()" href="" class="list-group-item"><span class="social-sprite facebook" style="margin-right: 10px;"></span>Facebook</a>
                    <a ng-click="requestFeature('twitter-sign-in')" href="" class="list-group-item"><span class="social-sprite twitter" style="margin-right: 10px;"></span>Twitter</a>
                    <a onclick="$('#yahooOpenId').submit()" href="" class="list-group-item"><span class="social-sprite yahoo" style="margin-right: 10px;"></span>Yahoo</a>
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
                        <button name="submit" type="submit" class="btn btn-default ng-binding">Register</button>
                    </div>
                </form>
            </div>

            <div class="col-sm-5 col-sm-offset-2">
                <h3 class="ng-binding">Use a Smigo account</h3>

                <form name="loginOrRegisterform" role="form" novalidate="" ng-submit="submitLoginOrRegisterForm(loginOrRegisterform,formModel,widgetId)" class="ng-pristine ng-invalid ng-invalid-required ng-valid-pattern ng-valid-minlength ng-valid-maxlength ng-invalid-equals ng-valid-parse">
                    <div class="has-error">
                        <!-- ngRepeat: error in loginOrRegisterform.objectErrors -->
                    </div>

                    <div class="form-group" ng-class="{'has-error':loginOrRegisterform.username.$invalid &amp;&amp; (loginOrRegisterform.username.$dirty || loginOrRegisterform.submitted)}">
                        <label for="username" class="ng-binding">Username</label>
                        <input type="text" class="form-control ng-pristine ng-untouched ng-invalid ng-invalid-required ng-valid-pattern ng-valid-minlength ng-valid-maxlength" id="username" name="username" placeholder="Username" ng-model="formModel.username" ng-required="true" ng-minlength="5" ng-maxlength="40" ng-pattern="/^[\w]+$/" required="required">

                        <!-- ngIf: loginOrRegisterform.username.$dirty || loginOrRegisterform.submitted -->
                    </div>

                    <div class="form-group" ng-class="{'has-error':loginOrRegisterform.password.$invalid &amp;&amp; (loginOrRegisterform.password.$dirty || loginOrRegisterform.submitted)}">
                        <label for="password" class="ng-binding">Password</label>
                        <input type="password" class="form-control ng-pristine ng-untouched ng-invalid ng-invalid-required ng-valid-minlength" id="password" name="password" placeholder="Password" ng-model="formModel.password" ng-required="true" ng-minlength="6" required="required">

                        <!-- ngIf: loginOrRegisterform.password.$dirty || loginOrRegisterform.submitted -->
                    </div>

                    <!-- ngIf: viewModel.register -->
                    <div class="form-group ng-scope" ng-if="viewModel.register" ng-class="{'has-error':loginOrRegisterform.passwordagain.$invalid &amp;&amp; loginOrRegisterform.passwordagain.$dirty}">
                        <label for="password-again" class="ng-binding">Verify new password</label>
                        <input type="password" class="form-control ng-pristine ng-untouched ng-invalid ng-invalid-equals" id="password-again" name="passwordagain" placeholder="Verify new password" ng-model="passwordAgain" equals="">

                        <!-- ngIf: loginOrRegisterform.passwordagain.$dirty -->
                    </div>
                    <!-- end ngIf: viewModel.register -->

                    <!-- ngIf: viewModel.register -->
                    <div class="form-group ng-scope" ng-if="viewModel.register">
                        <div id="recaptcha"></div>
                    </div>
                    <!-- end ngIf: viewModel.register -->

                    <!-- ngIf: viewModel.register -->
                    <div class="form-group checkbox ng-scope" ng-if="viewModel.register" ng-class="{'has-error':loginOrRegisterform.termsOfService.$invalid &amp;&amp; loginOrRegisterform.submitted}">
                        <label for="termsOfService" class="checkbox ng-binding">
                            <input type="checkbox" id="termsOfService" name="termsOfService" ng-model="formModel.termsOfService" required="" class="ng-pristine ng-untouched ng-invalid ng-invalid-required ng-valid-parse">
                            I agree to the terms of service
                            <a target="_blank" href="/static/tos.html" class="ng-binding">Terms of Service</a>
                        </label>
                        <span ng-show="loginOrRegisterform.termsOfService.$error.required &amp;&amp; loginOrRegisterform.submitted" class="help-block ng-binding ng-hide">Required</span>
                    </div>
                    <!-- end ngIf: viewModel.register -->

                    <!-- ngIf: viewModel.login -->

                    <button type="submit" class="btn btn-default ng-binding" ng-class="{disabled: loginOrRegisterform.processing}" id="submit-login-register-form">Register</button>
                    <!-- ngIf: loginOrRegisterform.processing -->
                </form>

                <!-- ngIf: viewModel.login -->

            </div>
        </div>

    </div>
</div>
<jsp:include page="../footer.jsp"/>