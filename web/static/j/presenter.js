
var DisplayType = {NONE: 0, SLIDE: 1, WHITEBOARD: 2, INTERACTIVITY: 3};

var SlideDisplay = Class.create();
SlideDisplay.prototype = {
	initialize: function(container) {
		this.container = $(container);
		this.baseImage = Builder.node('img', {style: 'width: 100%; display: none;'});
		this.overlayImage = Builder.node('img', {style: 'width: 100%; height: 100%; position: absolute; top: 0; left: 0; display: none;'});
		this.interactivityContainer = Builder.node('div', {style: 'width: 100%; display: none;'}, 'interactivity goes here');
		
		this.toolbar = Builder.node('p', [
			this.statusSpan = Builder.node('span', 'synched'),
			' ',
			this.pauseAnchor = Builder.node('a', {href: '#'}, 'pause'),
			this.playAnchor = Builder.node('a', {href: '#', style: 'display: none'}, 'play'),
			' ',
			this.addInkAnchor = Builder.node('a', {href: '#'}, 'add ink'),
			this.saveInkAnchor = Builder.node('a', {href: '#', style: 'display: none'}, 'save ink'),
			' ',
			this.clearInkAnchor = Builder.node('a', {href: '#', style: 'display: none'}, 'erase ink')
		]);
		
		Event.observe(this.pauseAnchor, 'click', function(e) {
			this.pause();
			Event.stop(e);
		}.bindAsEventListener(this));
		
		Event.observe(this.playAnchor, 'click', function(e) {
			this.play();
			Event.stop(e);
		}.bindAsEventListener(this));
		
		Event.observe(this.addInkAnchor, 'click', function(e) {
			this.addInk();
			Event.stop(e);
		}.bindAsEventListener(this));
		
		Event.observe(this.saveInkAnchor, 'click', function(e) {
			this.saveInk();
			Event.stop(e);
		}.bindAsEventListener(this));
		
		Event.observe(this.clearInkAnchor, 'click', function(e) {
			this.optionallyClearInk();
			Event.stop(e);
		}.bindAsEventListener(this));
		
		this.container.appendChild(this.baseImage);
		this.container.appendChild(this.interactivityContainer);
		this.container.appendChild(this.overlayImage);
		this.container.parentNode.appendChild(this.toolbar);
		
		this.baseType = DisplayType.NONE;
		this.interactivityType = DisplayType.NONE;
		this.overlayType = DisplayType.NONE;
		
		this.sync = true;
	},
	
	onLectureSessionStateChange: function(state) {
		if (state == null) {
			// TODO the lecture session is now inactive
		}
		else {
			if (this.sync) {
				this.displayState = state;
				if (state.currentInteractivityDefinitionId) {
					try {
					this.hideOverlay();
					this.showInteractivity(Builder.node('applet', {code: 'edu.uoregon.cs.p2presenter.interactivity.participant.InteractivityParticipantApplet', archive: prefix + '/static/interactivity.jar', width: 640, height: 480}, [
						Builder.node('param', {name: 'host', value: 'localhost'}),
						Builder.node('param', {name: 'interactivityDefinitionId', value: state.currentInteractivityDefinitionId})]));
						}catch(e) {alert(e);}
				}
				else if (state.currentWhiteboardId) {
					this.hideOverlay();
					if (state.currentWhiteboardInkCount > 0) {
						this.showBase(DisplayType.WHITEBOARD);
						
						this.baseImage.src = prefix + '/ink/whiteboards/' + state.currentWhiteboardId + '/' + (state.currentWhiteboardInkCount - 1) + '.png';
					}
					else {
						// TODO show a blank whiteboard instead
						this.hideBase();
					}
				}
				else if (state.currentSlideSessionId) {
					this.baseImage.src = prefix + '/slides/' + state.currentSlideId + '.png';
					if (state.currentSlideSessionInkCount > 0) {
						this.showOverlay(DisplayType.SLIDE);
						this.overlayImage.src = prefix + '/ink/slides/' + state.currentSlideSessionId + '/' + (state.currentSlideSessionInkCount - 1) + '.png';
					}
					else {
						this.hideOverlay();
					}
					
					this.showBase(DisplayType.SLIDE);
				}
				else {
					state = null;
					// TODO error
				}
			}
			this.state = state;
		}
	},
	
	hideBase: function() {
		if (this.baseType != DisplayType.NONE) {
			Element.hide(this.baseImage);
		}
		this.baseType = DisplayType.NONE;
	},
	
	showBase: function(baseType) {
		this.hideOverlay();
		if (this.baseType == DisplayType.NONE) {
			Element.show(this.baseImage);
		}
		this.baseType = baseType;
	},
	
	hideOverlay: function() {
		if (this.overlayType != DisplayType.NONE) {
			Element.hide(this.overlayImage);
		}
		this.overlayType = DisplayType.NONE;
	},
	
	showOverlay: function(overlayType) {
		if (this.overlayType == DisplayType.NONE) {
			Element.show(this.overlayImage);
		}
		this.overlayType = overlayType;
	},
	
	hideInteractivity: function() {
		if (this.interactivityType != DisplayType.NONE) {
			Element.hide(this.interactivityContainer);
			this.interactivityContainer.innerHTML = '';
		}
		this.interactivityType = DisplayType.NONE;
	},
	
	showInteractivity: function(element) {
		this.hideBase();
		if (this.interactivityType == DisplayType.NONE) {
			Element.show(this.interactivityContainer);
			this.interactivityType = DisplayType.INTERACTIVITY;
			this.interactivityContainer.innerHTML = '';
			this.interactivityContainer.appendChild(element);
		}
	},
	
	setStatusText: function(statusText) {
		this.statusSpan.innerHTML = statusText;
	},
	
	pause: function() {
		this.sync = false;
		
		Element.show(this.playAnchor);
		Element.hide(this.pauseAnchor);
		this.setStatusText('paused');
	},
	
	play: function() {
		if (this.optionallyClearInk()) {
			this.sync = true;
			this.onLectureSessionStateChange(this.state);
			
			Element.show(this.pauseAnchor);
			Element.hide(this.playAnchor);
			this.setStatusText('synched');
		}
	},
	
	addInk: function() {
		this.pause();
		this.setStatusText('inking');
		Element.hide(this.addInkAnchor);
		
		if (!this.inkOverlay) {
			this.inkOverlay = new InkOverlay(this.container.offsetWidth, this.container.offsetHeight, this.onInkAdded.bind(this));
		}
		
		this.inkOverlayElement = this.inkOverlay.getElement();
		this.container.appendChild(this.inkOverlayElement);
	},
	
	saveInk: function() {
		var postBody = 'slideSessionId=' + this.displayState.currentSlideSessionId;
		if (this.inkOverlay.supportsGetInkData()) {
			postBody += '&data=' + this.inkOverlay.getInkData();
		}
		postBody += '&ink=' + this.inkOverlay.getInkJson();
		new Ajax.Request(controllerUrl + '/submit/freeform', {
			method: 'post',
			postBody: postBody
		});
	},
	
	optionallyClearInk: function(message) {
		// TODO string table
		if (!this.inkOverlay || !this.inkOverlay.hasInk() || confirm(message || 'Are you sure you want to erase your ink?')) {
			this.clearInk();
			return true;
		}
		else {
			return false;
		}
	},
	
	clearInk: function() {
		if (this.inkOverlayElement) {
			this.container.removeChild(this.inkOverlayElement);
			this.inkOverlayElement = null;
			this.inkOverlay.clear();
			Element.show(this.addInkAnchor);
			Element.hide(this.saveInkAnchor, this.clearInkAnchor);
		}
		this.pause();
	},
	
	onInkAdded: function() {
		Element.show(this.saveInkAnchor, this.clearInkAnchor);
	}
};

var InkOverlay = Class.create();
InkOverlay.prototype = {
	initialize: function(width, height, onInkAdded) {
		this.canvas = Builder.node('canvas', {style: 'position: absolute; top: 0; left: 0', width: width, height: height});
		this.onInkAdded = onInkAdded;
		this.ctx = this.canvas.getContext('2d');
		this.ctx.strokeStyle = "rgb(0, 0, 200)";
		
		this.clear();
		
		Event.observe(this.canvas, 'mousedown', function(e) {
			this.penDown = true;
			var pos = getRelativeCoordinates(e, this.canvas);
			this.offset = {x: Event.pointerX(e) - pos.x, y: Event.pointerY(e) - pos.y};
			this.ctx.beginPath();
			this.ctx.moveTo(pos.x, pos.y);
			this.currentLine = new Array();
			this.currentLine.strokeStyle = this.ctx.strokeStyle;
			this.lines[this.lineIndex++] = this.currentLine;
			this.pointIndex = 0;
		}.bindAsEventListener(this));
		
		Event.observe(this.canvas, 'mousemove', function(e) {
			if (this.penDown) {
				var pos = {x: Event.pointerX(e) - this.offset.x, y: Event.pointerY(e) - this.offset.y};
				if (pos.x >= 0 && pos.x <= this.canvas.width && pos.y >= 0 && pos.y <= this.canvas.height) {
					this.ctx.lineTo(pos.x, pos.y);
					this.ctx.stroke();
					this.currentLine[this.pointIndex++] = {x: pos.x, y: pos.y};
				}	
				this.ctx.beginPath();
			
				this.ctx.moveTo(pos.x, pos.y);
			}
		}.bindAsEventListener(this));

		Event.observe(document, 'mouseup', function(e) {
			if (this.penDown && this.onInkAdded) {
				this.onInkAdded();
			}
			this.penDown = false;
		}.bindAsEventListener(this));
	},
	
	getElement: function() {
		return this.canvas;
	},
	
	hasInk: function() {
		return this.lineIndex != 0 ;
	},
	
	clear: function() {
		if (this.hasInk()) {
			this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
		}
		this.penDown = false;
		this.lines = new Array();
		this.lineIndex = 0;
	},
	
	getInkJson: function() {
		var result = '{"w":' + this.canvas.width + ',"h":' + this.canvas.height + ',"l":[';
		if (this.lines.length > 0) {
			result = appendLineString(this.lines[0], result);
			
			for (var i = 1; i < this.lines.length; i++) {
				result = appendLineString(this.lines[i], result + ',');
			}
		}
		
		return result + ']}';
	},
	
	supportsGetInkData: function() {
		return this.canvas.toDataURL ? true : false;
	},
	
	getInkData: function() {
		return this.canvas.toDataURL ? this.canvas.toDataURL() : null;
	}
};

var appendLineString = function(line, result) {
	result += '{"s":"' + line.strokeStyle + '","p":[';
	if (line.length > 0) {
		var point = line[0];
		result += point.x + ',' + point.y;
		
		for (var i = 1; i < line.length; i++) {
			point = line[i];
			result += ',' + point.x + ',' + point.y;
		}
	}
	return result + ']}';
};

var getAbsolutePosition = function(element) {
	var r = { x: element.offsetLeft, y: element.offsetTop };
	if (element.offsetParent) {
		var tmp = getAbsolutePosition(element.offsetParent);
		r.x += tmp.x;
		r.y += tmp.y;
	}
	return r;
};
	
	/**
	  * Retrieve the coordinates of the given event relative to the center
	  * of the widget.
	  *
	  * @param event
	  *  A mouse-related DOM event.
	  * @param reference
	  *  A DOM element whose position we want to transform the mouse coordinates to.
	  * @return
	  *    A hash containing keys 'x' and 'y'.
	  */
	   var getRelativeCoordinates = function(event, reference) {
	    var x, y;
	    var el = event.target || event.srcElement;
	    if (!window.opera && typeof event.offsetX != 'undefined') {
	      // Use offset coordinates and find common offsetParent
	      var pos = { x: event.offsetX, y: event.offsetY };
	      // Send the coordinates upwards through the offsetParent chain.
	      var e = el;
	      while (e) {
	        e.mouseX = pos.x;
	        e.mouseY = pos.y;
	        pos.x += e.offsetLeft;
	        pos.y += e.offsetTop;
	        e = e.offsetParent;
	      }
	      // Look for the coordinates starting from the reference element.
	      var e = reference;
	      var offset = { x: 0, y: 0 }
	      while (e) {
	        if (typeof e.mouseX != 'undefined') {
	          x = e.mouseX - offset.x;
	          y = e.mouseY - offset.y;
	          break;
	        }
	        offset.x += e.offsetLeft;
	        offset.y += e.offsetTop;
	        e = e.offsetParent;
	      }
	      // Reset stored coordinates
	      e = el;
	      while (e) {
	        e.mouseX = undefined;
	        e.mouseY = undefined;
	        e = e.offsetParent;
	      }
	    }
	    else {
	      // Use absolute coordinates
	      var pos = getAbsolutePosition(reference);
	      x = event.pageX  - pos.x;
	      y = event.pageY - pos.y;
	    }
	    return { x: x, y: y };
	  }
	
var redraw = function() {
	//ctx.clearRect(0,0, canvas.width, canvas.height);
	var point;
	ctx.save();
	ctx.scale(2,2);
	ctx.beginPath();
	if (points.length > 0) {
		point = points[0];
		ctx.moveTo(point.x, point.y);
	
		for (var i = 0; i < points.length; i++) {
			point = points[i];
			ctx.lineTo(point.x, point.y);
			ctx.stroke();
			ctx.beginPath();
			ctx.moveTo(point.x, point.y);
		}	
	}
	ctx.stroke();
	ctx.restore();
};