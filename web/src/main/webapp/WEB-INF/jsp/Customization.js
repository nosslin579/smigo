String.prototype.capitalize = function () {
    return this.charAt(0).toUpperCase() + this.slice(1);
};

Array.prototype.find = function (value, property, options) {
    if (options.ignoreCase) {
        for (var i = 0; i < this.length; i++) {
            if (this[i][property] && this[i][property].toLocaleLowerCase() == value.toLowerCase()) {
                return this[i];
            }
        }
    } else {
        for (var i = 0; i < this.length; i++) {
            if (this[i][property] == value) {
                return this[i];
            }
        }
    }
    return null;
};