function CommentService($log, $http, $rootScope, $q, $location) {
    'use strict';
    var state = {comments: []};

    $rootScope.$on('current-user-changed', function (event, newUser) {
        state.comments.length = 0;
        if (newUser) {
            var username = newUser && newUser.username;
            $http.get('/rest/comment', {params: {receiver: username}}).then(function (response) {
                Array.prototype.push.apply(state.comments, response.data);
            });
        }
    });


    return {
        addComment: function (comment) {
            //$scope.objectErrors = [];
            $http.post('/rest/comment', comment).then(function (response) {
                comment.id = response.data;
                state.comments.push(angular.copy(comment));
                comment.text = '';
            }).catch(function (error) {
                comment.objectErrors = error.data;
            });
        },
        deleteComment: function (comment) {
            $http.delete('/rest/comment/' + comment.id).then(function (response) {
                state.comments.splice(state.comments.indexOf(comment), 1);
                //for (var i = 0; i < state.comments.length; i++) {
                //    if (state.comments[i].id=comment.id) {
                //        state.comments.splice(i, 1);
                //    }
                //}
            }).catch(function (error) {
                $scope.objectErrors = error.data;
            });
        },
        getState: function () {
            return state;
        }
    };
}
angular.module('smigoModule').factory('CommentService', CommentService);