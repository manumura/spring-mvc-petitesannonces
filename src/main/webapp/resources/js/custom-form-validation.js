/**
 * Check if element has an attribute
 */
$.fn.hasAttr = function(name) {
	var attr = $(this).attr(name);
	return typeof attr !== typeof undefined && attr !== false;
};

/**
 * Display proper bootstrap class on input elements
 */
$.fn.customValidationDisplay = function(evt) {
	
	var formGroup = $(this).closest('.form-group');
	var glyphicon = formGroup.find('.glyphicon.form-control-feedback').first();
	
	var isValid = $(this).hasClass('valid');
	
	var isDataValidation = $(this).hasAttr('data-validation');
	var isOptional = $(this).hasAttr('data-validation-optional');
	var isEmpty = !$(this).val();
	
	var hasToBeValidated = isDataValidation && (!isOptional || (isOptional && !isEmpty));
	
	//	console.log(isValid + " " + hasToBeValidated + " " + $(this).attr('id'));
	
	if (hasToBeValidated) {
		if (isValid) {
			if (formGroup.length) {
				formGroup.removeClass('has-error').addClass('has-success');
			}
			if (glyphicon.length) {
				glyphicon.removeClass('glyphicon-remove').addClass('glyphicon-ok');
			}
		} else {
			if (formGroup.length) {
				formGroup.addClass('has-error').removeClass('has-success');
			}
			if (glyphicon.length) {
				glyphicon.removeClass('glyphicon-ok').addClass('glyphicon-remove');
			}
		}
	}
};

// Display proper bootstrap class on all input elements of the form
$.fn.displayFormValidation = function () {
	this.each(function() {
		var input = $(this);
		var type = input.attr('type');
		if (type !== 'submit' && type !== 'button') {
			input.customValidationDisplay(event);
		}
	});
};

//Keep track of last focused element
/*var lastFocusedElement;

$('input').on('focus', function(event) {
	lastFocusedElement = $(this);
});*/

/*$('form#registrationForm').keyup(function (e) {
    var ctrl = null;
    if (e.originalEvent.explicitOriginalTarget) { // FF
        ctrl = e.originalEvent.explicitOriginalTarget;
    }
    else if (e.originalEvent.srcElement) { // IE, Chrome and Opera
        ctrl = e.originalEvent.srcElement;
    }
    console.log(ctrl.attr('id'));
});*/

//GET to POST submit
/*$("a.post").click(function(e) {
     e.stopPropagation();
     e.preventDefault();
     var href = this.href;
     var parts = href.split('?');
     var url = parts[0];
     var params = parts[1].split('&');
     var pp, inputs = '';
     for(var i = 0, n = params.length; i < n; i++) {
         pp = params[i].split('=');
         inputs += '<input type="hidden" name="' + pp[0] + '" value="' + pp[1] + '" />';
     }
     $("body").append('<form action="'+url+'" method="post" id="poster">'+inputs+'</form>');
     $("#poster").submit();
 });
<a class="post" href="reflector?color=blue&weight=340&model=x-12&price=14.800">Post it!</a>*/

// [name] is the name of the event "click", "mouseover", ... 
// same as you'd pass it to bind()
// [fn] is the handler function
/*$.fn.bindFirst = function(name, fn) {
    // bind as you normally would
    // don't want to miss out on any jQuery magic
    this.on(name, fn);

    // Thanks to a comment by @Martin, adding support for
    // namespaced events too.
    this.each(function() {
        var handlers = $._data(this, 'events')[name.split('.')[0]];
        // take out the handler we just inserted from the end
        var handler = handlers.pop();
        // move it at the beginning
        handlers.splice(0, 0, handler);
    });
};*/