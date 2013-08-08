if(!('contains' in String.prototype)) {
	String.prototype.contains = function(str, startIndex) { return -1 !== String.prototype.indexOf.call(this, str, startIndex); };
}