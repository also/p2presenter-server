
var LectureDisplay = Class.create();
LectureDisplay.prototype = {
	initialize: function(elt, lectureUrl) {
		this.elt = new Element('div', {'class': 'lectureDisplay'});
		$(elt).appendChild(this.elt);

		this.layout = new SplitLayout(SplitLayout.HORIZONTAL, 'px');
		this.elt.appendChild(this.layout.getElement());

		this.slideListElt = new Element('div', {'class': 'slideList'});
		this.layout.addA(this.slideListElt, 230);

		this.layout.addB(new Element('div'), 230);

		this.centerLayout = new SplitLayout(SplitLayout.VERTICAL, '%');
		this.layout.setCenter(this.centerLayout);

		this.slideDisplay = this.createSlideDisplay();
		this.centerLayout.setCenter(this.slideDisplay);

		this.slideDetailsDisplay = this.createSlideDetailsDisplay();
		this.centerLayout.addB(this.slideDetailsDisplay, 25, {inset: '10px'});

		this.loadLecture(lectureUrl);
	},

	loadLecture: function(lectureUrl) {
		new Ajax.Request(lectureUrl, {
			onSuccess: function(transport) {
				this.lecture = transport.responseText.evalJSON();
				this.lecture.slides.each(function(slide) {
					var src = Routes.url('slideThumbnailImage', {id: slide.id});
					var slideThumbnailElt = new Element('img', {'src': src});
					slideThumbnailElt.observe('click', function() {
						this.showSlide(slide);
					}.bind(this));
					this.slideListElt.appendChild(slideThumbnailElt);
				}.bind(this));
			}.bind(this)
		});
	},

	createSlideDisplay: function() {
		return new SimpleLectureSlideDisplay();
	},

	createSlideDetailsDisplay: function() {
		return new SimpleLectureSlideDetailsDisplay();
	},

	showSlide: function(slide) {
		this.slideDisplay.setCurrentSlide(slide);
		this.slideDetailsDisplay.setCurrentSlide(slide);
	}
};

var AbstractDisplayItem = Class.create({
	initialize: function() {
		this.elt = this.createElement();
	},

	getElement: function() {
		return this.elt;
	}
});

var SimpleLectureSlideDisplay = Class.create({
	initialize: function() {
		this.elt = new Element('div', {'class': 'slideDisplay'});
		this.slideImageRoute = Routes.get('slideImage');
	},

	getElement: function() {
		return this.elt;
	},

	setCurrentSlide: function(slide) {
		this.elt.update();

		this.elt.appendChild(new Element('img', {'src': this.slideImageRoute.parameterize({id: slide.id})}));
	}
});

var SimpleLectureSlideDetailsDisplay = Class.create({
	initialize: function() {
		this.elt = new Element('div', {'class': 'slideDetails'});
		this.slideTitleElt = new Element('h2');
		this.slideTitleElt.update('&lt;title&gt;');
		this.elt.appendChild(this.slideTitleElt);

		this.slideBodyElt = new Element('p');
		this.slideBodyElt.update('&lt;body&gt;');
		this.elt.appendChild(this.slideBodyElt);

		var buttonsElt = new Element('p', {'class': 'actions'});
		this.editButtonElt = new Element('a');
		this.editButtonElt.update('Edit');
		this.editButtonElt.observe('click', this.showEditor.bind(this));
		buttonsElt.appendChild(this.editButtonElt);
		this.elt.appendChild(buttonsElt);
	},

	getElement: function() {
		return this.elt;
	},

	setCurrentSlide: function(slide) {
		this.slide = slide;
		this.slideTitleElt.update(slide.title);
		this.slideBodyElt.update(slide.body);
	},

	showEditor: function(e) {
		e.stop();
		if (this.slide) {
			var newTitle = prompt('Slide title', this.slide.title);
		}
	}
});