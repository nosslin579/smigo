<%@page pageEncoding="UTF-8" contentType="text/html; charset=UTF-8" %>
<jsp:include page="../header.jsp"/>
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

    </div>
</div>
<jsp:include page="../footer.jsp"/>