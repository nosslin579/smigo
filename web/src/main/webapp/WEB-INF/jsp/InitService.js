function InitService() {

    return  {
        user: <c:out value="${f:toJson(user)}"/>,
        garden: <c:out escapeXml="false" value="${f:toJson(garden)}"/>
    }
}
angular.module('smigoModule').factory('InitService', InitService);