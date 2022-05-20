function change() {

	var element;
	if (document.getElementById("search_box") === "") {
		element = document.getElementById("search_button");
		element.disabled = true;
	} else {
		element = document.getElementById("search_button");
		element.disabled = false;
	}

}
