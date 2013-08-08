;(function(doc) {
	var el = doc.createElement("detect");
	if (el.style.webkitFlexWrap === "" || el.style.mozFlexWrap === "" || el.style.msFlexWrap === "" || el.style.oFlexWrap === "" || el.style.flexWrap === "") {
		doc.getElementsByTagName("html")[0].className += " flexwrap";
	}
})(window.document);
