;(function($) {
	var search = $('<input>');
	search
		.addClass('filter-list')
		.attr('placeholder', 'Digite para filtrar')
		.prependTo('.courses')
		.after('<small class="filter-warning">Para uma melhor usabilidade, <strong>não</strong> utilize o Firefox (<a href="https://bugzilla.mozilla.org/show_bug.cgi?id=702508">culpa desse bug</a>)</small>')
		.filterList(search.parent().find('ul'), 'h2');
})(Zepto);