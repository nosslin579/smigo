<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:include page="../header.jsp"/>
<jsp:include page="nav.jsp"/>

<div ng-view="" class="angular-view ng-scope">
    <div class="container ng-scope">
        <div class="page-header text-center">
            <h1 class="ng-binding">
                Faq
            </h1>
        </div>

        <h3 class="ng-binding">What is this?</h3>
        <p class="ng-binding">-Smigo is a garden planner for vegetables and flowers. It will give you advice about companion planting and crop rotation. You can also share your plan with your friends.</p>

        <h3 class="ng-binding">How does it work?</h3>
        <p class="ng-binding">
            1. Select a plant in the left column by clicking on it.<br>
            2. Click somewhere in the grid. Now you have added your first plant.<br>
            3. Add more plants until your grid equals your garden.<br>
            4. Register and share your plan with friends.<br>
            <a href="/wall/example" class="ng-binding">View an example</a>
        </p>

        <h3 class="ng-binding">How do I delete a plant?</h3>
        <p class="ng-binding">-Hold shift and click square or click the "delete square"-button and then click the square.</p>

        <h3 class="ng-binding">Is it for free?</h3>
        <p class="ng-binding">-Yes it is. And tell all your friends about it.</p>

        <h3 class="ng-binding">My question is not listed here. What do I do?</h3>
        <p class="ng-binding">-Leave a note in the
            <a class="text-lowercase ng-binding" href="/forum">Forum</a> or contact me via:</p>

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
                Known bugs
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
                                <div style="text-decoration: underline;" class="ng-binding">About</div>
                                <a href="https://github.com/nosslin579/smigo" class="ng-binding">Source code</a><br>
                                <a href="/static/terms-of-service.html" rel="nofollow" class="ng-binding">Terms of Service</a><br>
                                <a href="/help" class="ng-binding">Help</a><br>
                                <a href="/forum" class="ng-binding">Forum</a><br>
                            </div>
                            <div class="col-xs-4">
                                <div style="text-decoration: underline;" class="ng-binding">Contact</div>
                                <a href="http://www.reddit.com/r/smigo" target="_blank">Reddit</a><br>
                                <a href="https://www.facebook.com/smigogarden" target="_blank">Facebook</a><br>
                                <a href="https://www.twitter.com/smigogarden" target="_blank">Twitter</a><br>
                                <a href="http://se.linkedin.com/pub/christian-nilsson/3b/798/a5b/" target="_blank">Linkedin</a><br>
                            </div>
                            <div class="col-xs-4 col-xs-3">
                                <div style="text-decoration: underline;" class="ng-binding">Links</div>
                                <a href="http://en.wikipedia.org/wiki/Companion_planting" target="_blank" class="ng-binding">Companion planting</a><br>
                                <a href="http://en.wikipedia.org/wiki/Square_foot_gardening" target="_blank" class="ng-binding">Square foot gardening</a><br>
                                <a href="http://sourceforge.net/projects/kitchengarden" target="_blank" class="ng-binding">Kitchen garden aid</a><br>
                                <a href="http://en.wikipedia.org/wiki/Crop_rotation" target="_blank" class="ng-binding">Crop rotation</a><br>
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
<jsp:include page="footer.jsp"/>