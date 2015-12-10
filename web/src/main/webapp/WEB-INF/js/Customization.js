//todo rename
Object.defineProperty(String.prototype, 'capitalize', {
    enumerable: false,
    configurable: false,
    writable: false,
    value: function () {
        return this.charAt(0).toUpperCase() + this.slice(1);
    }
});

Object.defineProperty(Array.prototype, 'smigoLast', {
    enumerable: false,
    configurable: false,
    writable: false,
    value: function () {
        if (this.length > 0) return this[this.length - 1];
    }
});

Object.defineProperty(Array.prototype, 'smigoFind', {
    enumerable: false,
    configurable: false,
    writable: false,
    value: function (value, property) {
        for (var i = 0; i < this.length; i++) {
            if (this[i][property] === value) {
                return this[i];
            }
        }
        return null;
    }
});