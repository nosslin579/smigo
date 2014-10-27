function SortRuleFilter($log, orderByFilter) {
    return function (ruleArray) {
        return ruleArray.sort(function (r1, r2) {
            var number = r1.ruleType - r2.ruleType;
            return number ? number : r1.id - r2.id;
        });
    };
}

angular.module('smigoModule').filter('sortruleFilter', SortRuleFilter);