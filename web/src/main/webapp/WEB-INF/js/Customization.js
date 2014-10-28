Object.defineProperty(String.prototype, 'capitalize', {
    enumerable: false,
    configurable: false,
    writable: false,
    value: function () {
        return this.charAt(0).toUpperCase() + this.slice(1);
    }
});

Object.defineProperty(Array.prototype, 'last', {
    enumerable: false,
    configurable: false,
    writable: false,
    value: function () {
        if (this.length > 0) return this[this.length - 1];
    }
});

Object.defineProperty(Array.prototype, 'find', {
    enumerable: false,
    configurable: false,
    writable: false,
    value: function (value, property, options) {

        if (options && options.ignoreCase) {
            for (var i = 0; i < this.length; i++) {
                if (this[i][property] && this[i][property].toLocaleLowerCase() == value.toLowerCase()) {
                    return this[i];
                }
            }
        } else {
            for (var i = 0; i < this.length; i++) {
                if (this[i][property] === value) {
                    return this[i];
                }
            }
        }
        return null;
    }
});