function InitService() {

    return  {
        messages: <c:out escapeXml="false" value="${f:toJson(messages)}" />,
        user: <c:out escapeXml="false" value="${f:toJson(user)}"/>,
        garden: <c:out escapeXml="false" value="${f:toJson(garden)}"/>
    }
}
angular.module('smigoModule').factory('InitService', InitService);