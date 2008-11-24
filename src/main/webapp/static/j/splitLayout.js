var SplitLayout = Class.create({
	initialize: function(orientation, units, options) {
		this.orientation = orientation;
		this.units = units;

		this.currentOffset = {
			a: 0,
			b: 0
		};

		this.options = {
			separator: '1px solid #aaa'
		};
		Object.extend(this.options, options || {});

		this.oppositeOrientation = (orientation == SplitLayout.HORIZONTAL) ? SplitLayout.VERTICAL : SplitLayout.HORIZONTAL;

		Object.extend(this, SplitLayout[orientation]);
		this.opposite = SplitLayout[this.oppositeOrientation];

		this.elt = new Element('div', {
			'class': 'splitLayout',
			style:
				'position:absolute;width:100%;height:100%'
		});

		this.centerWrapperElt = new Element('div', {
			'class': 'splitLayoutCenter',
			style:
				'position:absolute;' +
				this.offsetAnchors['a'] + ':0;' +
				this.offsetAnchors['b'] + ':0;' +
				this.opposite.dimension + ':100%;' +
				'overflow:auto;'
		});
		this.elt.appendChild(this.centerWrapperElt);
	},

	getElement: function() {
		return this.elt;
	},

	addA: function(elt, size, options) {
		this._addAnchoredElt('a', elt, size, options);
	},

	addB: function(elt, size, options) {
		this._addAnchoredElt('b', elt, size, options);
	},

	setCenter: function(elt) {
		this.centerWrapperElt.update();
		this.centerWrapperElt.appendChild(this._toElt(elt));
	},

	_toElt: function(elt) {
		if (elt.getElement) {
			elt = elt.getElement();
		}

		return elt;
	},

	_addAnchoredElt: function(pos, elt, size, options) {
		var tmpOptions = {};
		Object.extend(tmpOptions, this.options);
		Object.extend(tmpOptions, options || {});
		options = tmpOptions;

		elt = this._toElt(elt);
		var wrapperElt = new Element('div', {
			'class': 'splitLayoutAnchored',
			style:
				'position:absolute;' +
				this.offsetAnchors[pos] + ':' + this.currentOffset[pos] + this.units + ';' +
				this.opposite.offsetAnchors[pos] + ':0;' +
				this.dimension + ':' + size + this.units + ';' +
				'overflow:auto;' +
				this.opposite.dimension + ':100%;'
		});

		this.elt.appendChild(wrapperElt);

		if (options.separator) {
			var borderElt = new Element('div', {
				'class': 'splitLayoutBorder',
				style:
					'border-' + this.offsetAnchors[SplitLayout.oppositePos[pos]] + ':' + options.separator + ';' +
					this.opposite.dimension + ':100%;'
			});

			wrapperElt.appendChild(borderElt);
			wrapperElt = borderElt;
		}

		if (options.inset) {
			var inset = SplitLayout.toInsets(options.inset);
			var insetElt = new Element('div', {
				'class': 'splitLayoutInset',
				style:
					'position:absolute;' +
					'top:' + inset.top + ';' +
					'right:' + inset.right + ';' +
					'bottom:' + inset.bottom + ';' +
					'left:' + inset.left + ';'
			});

			wrapperElt.appendChild(insetElt);
			wrapperElt = insetElt;
		}
		wrapperElt.appendChild(elt);

		this._setOffset(pos, this.currentOffset[pos] + size);
	},

	_setOffset: function(pos, offset) {
		this.currentOffset[pos] = offset;
		this.centerWrapperElt.style[this.offsetAnchors[pos]] = offset + this.units;
	}
});

Object.extend(SplitLayout, {
	HORIZONTAL: 'h',
	VERTICAL: 'v',
	DEFAULT_INSETS: {
		top: '0',
		right: '0',
		bottom: '0',
		left: '0'
	},
	oppositePos: {
		a: 'b',
		b: 'a'
	},
	h: {
		dimension: 'width',
		offsetAnchors: {
			a: 'left',
			b: 'right'
		}
	},
	v: {
		dimension: 'height',
		offsetAnchors: {
			a: 'top',
			b: 'bottom'
		}
	},

	toInsets: function(insets) {
		if (insets.top || insets.right || insets.bottom || insets.left) {
			// clone
			var result = Object.extend({}, SplitLayout.DEFAULT_INSETS);
			Object.extend(result, insets);

			return result;
		}
		else if (Object.isArray(insets)) {
			if (insets.length == 2) {
				return {
					top: insets[0],
					right: insets[1],
					bottom: insets[0],
					right: insets[1]
				};
			}
			if (insets.length == 4) {
				return {
					top: insets[0],
					right: insets[1],
					bottom: insets[2],
					right: insets[3]
				};
			}
		}
		else if (Object.isString(insets)) {
			if (insets.indexOf(' ') == -1) {
				return {
					top: insets,
					right: insets,
					bottom: insets,
					left: insets
				};
			}
			else {
				return this._toInsets(insets.split(' '));
			}
		}

		return null;
	}
});

SplitLayout.prototype.addTop = SplitLayout.prototype.addLeft;
SplitLayout.prototype.addBottom = SplitLayout.prototype.addRight;