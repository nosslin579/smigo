<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<nav class="navbar navbar-default ng-scope" role="navigation" ng-controller="MainMenuController">
    <!-- ngIf: !uglyHackToReloadMenu -->
    <div class="container-fluid ng-scope" ng-if="!uglyHackToReloadMenu">
        <div class="navbar-header">
            <button type="button" class="navbar-toggle" ng-click="menuIsCollapsed = !menuIsCollapsed">
                <span class="sr-only">Toggle navigation</span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
                <span class="icon-bar"></span>
            </button>
            <a class="navbar-brand" href="/">Smigo<span class="hidden-sm hidden-xs ng-binding"> - Planera din köksträdgård</span></a>
        </div>
        <div class="navbar-collapse collapse" collapse="menuIsCollapsed" style="height: 0px;">
            <ul class="nav navbar-nav navbar-right">
                <li><a href="/garden" class="ng-binding">Trädgård</a></li>
                <li><a href="/forum" id="forum-link" class="ng-binding">Forum</a></li>
                <li><a href="/help" class="ng-binding">Hjälp</a></li>
                <li ng-class="{hidden: userState.currentUser}">
                    <a href="/register" id="register-link" class="ng-binding">Registrera</a></li>
                <li ng-class="{hidden: userState.currentUser}">
                    <a href="/login" id="login-link" class="ng-binding">Logga in</a></li>
                <li ng-class="{hidden: !userState.currentUser}" class="hidden">
                    <a href="/account" id="account-link" class="ng-binding">Inställningar</a></li>
                <li ng-class="{hidden: !userState.currentUser}" class="hidden">
                    <a ng-click="logout()" href="" id="logout-link" class="ng-binding">Logga ut</a></li>
            </ul>
        </div>
    </div>
    <!-- end ngIf: !uglyHackToReloadMenu -->
</nav>
