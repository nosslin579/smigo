function smigolog(str, obj) {
  if (window.console)
    console.log(str, obj);
}
function smigowarning(str, obj) {
  if (window.console)
    console.log(str, obj);
}
function smigoerror(str, obj) {
  if (window.console)
    console.log(str, obj);
}

Array.prototype.remove = function (obj) {
    var indexOf = this.indexOf(obj);
    if (indexOf === -1) {
        return false;
    }
    this.splice(indexOf, 1);
    return true;
};

/*
(function ($) {
  $.fn.hasScrollBar = function () {
    return this.get(0).scrollHeight > this.height();
  }
})(jQuery);

(function ($) {
  $.fn.extractIdVariable = function () {
    smigolog("extractIdVariable fro " + this.attr('id'), this);
    return +this.attr('id').split('_')[1];
  }
})(jQuery);

(function ($) {
  $.fn.setDisplayToBlockIfParentWidthIsLarger = function () {
    var $this = $(this);
    smigolog('this ', $this.width());
    smigolog('parent x 2 ', $this.parent().parent().parent().width());
//		smigolog('Comparing width' + $this.width() + ' ' + $this.parent().get(0).width());
    return this;
  }
})(jQuery);*/
