/* CKeditor.js

	Purpose:

	Description:

	History:
		Tue Oct 7 17:56:45     2009, Created by jimmyshiau

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
delete CKEDITOR.focusManager._.blurDelay;

// Issue 18: Closing a modal window containing ckeditor causes js error  
zk.afterLoad('zul.wnd', function() {
	var _zjq = {};
	zk.override(zjq.prototype, _zjq, {
		undoVParent: function() {
			zWatch.fireDown('beforeUndoVParent', this);
			_zjq.undoVParent.apply(this, arguments);
		}
	});	
});

ckez.CKeditor = zk.$extends(zul.Widget, {
	_height: '200',
	_value: '',
	
	$define: {
		value: [function(v) {
			return !v ? '' : v;
		}, function (v) {
			var editor = this.getEditor();
			if (editor)
				editor.setData(v);
		}],
		autoHeight: null,
		customConfigurationsPath: _zkf = function () {
			if (this.desktop)
				this.rerender();
		},
		config: _zkf,
		toolbar: _zkf,
		width: function (v) {
			if (!v || !this.$n()) return;
			this._setSize(jq('#cke_' + this.uuid + '-cnt'), v, 'width');
		},
		height: function (v) {
			if (!v || !this.$n()) return;			
			this._setSize(jq('#cke_' + this.uuid + '-cnt'), v, 'height');
		}
	},	

	setVflex: function (v) {
		if (v == 'min') v = false;
		// set vflex if editor is prepared,
		// or sotre it in temp value.
		if (this._editor)
			this.$super(ckez.CKeditor, 'setVflex', v);
		else
			this._tmpVflex = v;
	},
	setHflex: function (v) {
		if (v == 'min') v = false;
		// set hflex if editor is prepared,
		// or sotre it in temp value.
		if (this._editor)
			this.$super(ckez.CKeditor, 'setHflex', v);
		else
			this._tmpHflex = v;
	},
	setFlexSize_: function(sz, ignoreMargins) {
		if (this._editor) {
			var n = this.$n(),
				zkn = zk(n);
			if (sz.height !== undefined) {
				if (sz.height == 'auto')
					n.style.height = '';
				else if (sz.height != '') //bug #2943174, #2979776
					this.setFlexSizeH_(n, zkn, sz.height, ignoreMargins);
				else {
					n.style.height = this._height || '';
					if (this._height)
						this._setSize(jq('#cke_' + this.uuid + '-cnt'), this._height, 'height');
					else
						this._setSize(jq('#cke_' + this.uuid + '-cnt'), '200px', 'height');
				}
			}
			if (sz.width !== undefined) {
				if (sz.width == 'auto')
					n.style.width = '';
				else if (sz.width != '') //bug #2943174, #2979776
					this.setFlexSizeW_(n, zkn, sz.width, ignoreMargins);
				else {
					n.style.width = this._width || '';
					if (this._width)
						this._setSize(jq('#cke_' + this.uuid + '-cnt'), this._width, 'width');
					else
						this._setSize(jq('#cke_' + this.uuid + '-cnt'), '100%', 'width');
				}
			}
			return {height: n.offsetHeight, width: n.offsetWidth};
		}
	},
	setFlexSizeH_: function(n, zkn, height, ignoreMargins) {
		// store height in temp value because setFlexSizeW_
		// might change topHeight then reset height again.
		this._hflexHeight = height;

		this.$super(ckez.CKeditor, 'setFlexSizeH_', n, zkn, height, ignoreMargins);
		var h = parseInt(n.style.height); // get parent setted height
		// remove outer div height so container like groupbox can change size with it
		n.style.height = '';
		// compute text area height
		// B-CKEZ-14: CKEditor Vflex doesn't work properly
		var textArea  = jq(n).find('.cke_contents'),
			topHeight = textArea.prev().outerHeight(), // top menu buttons
			bottomHeight = textArea.next().outerHeight();
		h = h - topHeight - bottomHeight;

		// set text area height
		this._setSize(textArea, jq.px0(h), 'height');
	},
	setFlexSizeW_: function(n, zkn, width, ignoreMargins) {
		// get current topHeight
		var topHeight = jq('#cke_' + this.uuid + '-cnt .cke_top').outerHeight();

		this.$super(ckez.CKeditor, 'setFlexSizeW_', n, zkn, width, ignoreMargins);
		var w = parseInt(n.style.width); // get parent setted width

		// set content width
		//w = w - 16;
		this._setSize(jq('#cke_' + this.uuid + '-cnt'), jq.px0(w), 'width');
		// set height again if topHeight changed
		// ignore if vflex not setted
		if (topHeight != jq('#cke_' + this.uuid + '-cnt .cke_top').outerHeight() && this._vflex)
			this.setFlexSizeH_(n, zkn, this._hflexHeight, ignoreMargins);
	},
	redraw: function (out) {
		out.push('<div', this.domAttrs_({domStyle: true}), '><textarea id="', this.uuid, '-cnt">', this._value, '</textarea></div>');
	},
	
	domAttrs_: function (no) {
		var attr = this.$supers('domAttrs_', arguments);
		if (!this.isVisible() && (!no || !no.visible))
			attr += ' style="display:none;"';
		return attr;
	},
	
    bind_ : function() {
		this.$supers('bind_', arguments);
		var wgt = this;
		setTimeout(function(){wgt._init();},50);
		zWatch.listen({onSend : this});
		zWatch.listen({onRestore : this});
		
		// Issue 18: Closing a modal window containing ckeditor causes js error
		zWatch.listen({beforeUndoVParent : this});
	},
	
	unbind_ : function() {
		if (!this._editor) {//bug 3048386: detach ckeditor before it loaded cause js error
			this._unbind = true;
			this._arguments = arguments;
			return;			
		}
	
		if (!this._destroyed)
			this._editor.destroy(true);
		

		this._unbind = this._editor = this._tmpVflex = this._tmpHflex = null;
		zWatch.unlisten({onSend : this});
		zWatch.unlisten({onRestore : this});
		
		// Issue 18: Closing a modal window containing ckeditor causes js error
		zWatch.unlisten({beforeUnbindWindow : this});
		
		this.$supers('unbind_', arguments);
	},
	
	// Issue 18: Closing a modal window containing ckeditor causes js error
	beforeUndoVParent: function() {
		for (var p = this.parent; p; p = p.parent) {
			if (p.$instanceof(zul.wnd.Window)) {
				this._editor.destroy(true);				
				this._destroyed = true;
			}
		}		
	},
	
	onSend: function (ctrl) {
		var implicit = ctrl.args[1];
		//don't send back if implicit (such as onTimer)
		if (!implicit) {
			var editor = this.getEditor();
			if (editor)
				this.$class.onBlur(editor, true);
		}
	},
	
	onRestore: function () {
		var iframe = jq('#cke_' + this.uuid + '-cnt iframe')[0];
		if (!iframe) return;
		
		CKEDITOR.remove( this._editor );
		jq(this.$n()).html('<textarea id="' + this.uuid + '-cnt">' + this._value + '</textarea>');
		this.clearCache();
		this._init();
		
		if (zk.ie)
			jq('#cke_' + this.uuid + '-cnt').width(jq('#cke_'+this.uuid+'-cnt').width());
	},
	
	_setSize: function (node, value, prop) {
		value = this._getValue(value);
		if (!value) return;
		
		node[prop](value);
		this._editor.config[prop] = value;
	},
	
	_getValue: function (value) {
		if (!value) return null;
		if (value.endsWith('%'))
			return zk.ie ? jq.px0(jq(this.$n()).width()) : value;
			
		return jq.px0(zk.parseInt(value));
	},
	
	getEditor: function () {
		return this._editor;
	},
	
	/** Returns the config used to instantiate
	 * @param config - the default config 
	 * @return the config used to instantiate
	 */
	getConfig_: function(config) {
		return config;
	}, 
		
	_init: function() {		
		var wgt = this,
			uuid = this.uuid,
			dtid = this.desktop.id,
			customConfigPath = this._customConfigurationsPath,
			filebrowserBrowseUrl = this.filebrowserBrowseUrl,
			filebrowserImageBrowseUrl = this.filebrowserImageBrowseUrl,
			filebrowserFlashBrowseUrl = this.filebrowserFlashBrowseUrl,
			filebrowserUploadUrl = this.filebrowserUploadUrl,
			filebrowserImageUploadUrl = this.filebrowserImageUploadUrl,
			filebrowserFlashUploadUrl = this.filebrowserFlashUploadUrl,
			fileBrowserTempl = this.fileBrowserTempl,
			fileUploadTempl = this.fileUploadTempl,
			config = this.getConfig_({
				customConfig: customConfigPath,
				width: this._getValue(this._width),
				height: this._getValue(this._height)
			});
		
		if (this._config) {
			zk.override(config, {}, this._config);
		}
		
		if (filebrowserBrowseUrl)
			config.filebrowserBrowseUrl = fileBrowserTempl + '?Type=Files&url=' + filebrowserBrowseUrl;

		if (filebrowserImageBrowseUrl)
			config.filebrowserImageBrowseUrl = fileBrowserTempl + '?Type=Images&url=' + filebrowserImageBrowseUrl;

		if (filebrowserFlashBrowseUrl)
			config.filebrowserFlashBrowseUrl = fileBrowserTempl + '?Type=Flash&url=' + filebrowserFlashBrowseUrl;
		
		if (filebrowserUploadUrl)
			config.filebrowserUploadUrl = fileUploadTempl + '?Type=Files&url=' + filebrowserUploadUrl + 
				'&dtid=' + dtid + '&uuid=' + uuid;

		if (filebrowserImageUploadUrl)
			config.filebrowserImageUploadUrl = fileUploadTempl + '?Type=Images&url=' + filebrowserImageUploadUrl + 
				'&dtid=' + dtid + '&uuid=' + uuid;

		if (filebrowserFlashUploadUrl)
			config.filebrowserFlashUploadUrl = fileUploadTempl + '?Type=Flash&url=' + filebrowserFlashUploadUrl + 
				'&dtid=' + dtid + '&uuid=' + uuid;
		
		if (this._toolbar)
			config.toolbar = this._toolbar;
			
		
		jq(this.$n('cnt')).ckeditor(function(){
			if (wgt._unbind) {
				this.destroy();
				wgt.unbind = wgt._editor = null;
				zWatch.unlisten({onSend : wgt});
				zWatch.unlisten({onRestore : wgt});
				wgt.$supers('unbind_', wgt._arguments);
				return;
			}
			wgt._editor = this;
			this.on('focus', ckez.CKeditor.onFocus);
			this.on('blur', ckez.CKeditor.onBlur);
			this.on('selectionChange', ckez.CKeditor.onSelection);
			wgt._overrideFormSubmit();		
			this.on('key', ckez.CKeditor.onAutoHeight); //on press any key
			this.on('loadSnapshot', ckez.CKeditor.onAutoHeight);//on Redo And Undo
			this.on('beforePaste', ckez.CKeditor.onAutoHeight);
			this.resetDirty();

			// restore tmp value while rerendered
			if (!wgt._tmpHflex && wgt._hflex) {
				wgt._tmpHflex = wgt._hflex;
				wgt.setHflex(null);
			}
			if (!wgt._tmpVflex && wgt._vflex) {
				wgt._tmpVflex = wgt._vflex;
				wgt.setVflex(null);
			}

			if (wgt._tmpHflex) {
				wgt.setHflex(wgt._tmpHflex, {force:true});
				wgt._tmpHflex = null;
			}
			if (wgt._tmpVflex) {
				wgt.setVflex(wgt._tmpVflex, {force:true});
				wgt._tmpVflex = null;
			}
		}, config);
	},
	
	_overrideFormSubmit: function() {
		var editor = this.getEditor(),	
			wgt = zk.Widget.$(editor.element.getId()),
			element = editor.element,
			form = element.$.form && new CKEDITOR.dom.element(element.$.form);
		
		if (!form) return;
		
		form.$.submit = CKEDITOR.tools.override(form.$.submit, function(originalSubmit) {
			return function() {
				editor.updateElement();
				var val = editor.getData();
				wgt.fire('onChange', {value: val}, {sendAhead: true});
				wgt.fire('onSave', {value: val}, {sendAhead: true});
			};
		});
	}
}, {
	onFocus: function (event) {
		var editor = event.editor,
			wgt = zk.Widget.$(editor.element.getId()),
			tmp = editor._.previousValue;
			
		wgt._tidChg = setInterval(function () {
			if (tmp != editor._.previousValue)			
				tmp = wgt.previousValue = editor._.previousValue;
				
			if (editor.mayBeDirty && wgt.previousValue != editor.getData()) {
				wgt.fire('onChanging', {
					value: editor.getData(),
					start: 0,
					bySelectBack: false
					},
				{ignorable:1}, 100);
				
				if (editor.mayBeDirty)
					wgt.previousValue = editor.getData();
			}			
		}, 500);
	},
	
	onBlur: function (event, ahead) {
		var editor = event.editor ? event.editor : event,
			wgt = zk.Widget.$(editor.element.getId());
			
		if (wgt._tidChg) {
			clearInterval(wgt._tidChg);
			wgt._tidChg = null;
		}
		
		if (editor.checkDirty()) {
			var val = editor.getData();
			wgt._value = val; //save for onRestore
			wgt.fire('onChange', {value: val}, {sendAhead: ahead ? ahead : true});
			editor.resetDirty();
		}
	},
	
	onSelection: function (event) {
		
		var editor = event.editor,
			wgt = zk.Widget.$(editor.element.getId()),
			selection = editor.getSelection();		
		
		
		if (!zk(wgt).isRealVisible) return;
		selection = CKEDITOR.env.ie ? selection.getNative().createRange().text: 
									selection.getNative().toString();		
		
		if (selection == '') return;
		
		//unimplemented, because it just fire on select a html tag
//		zk.log(selection);
//		wgt.fire('onSelection', {
//			start: 0,
//			end: 0,
//			selected: selection
//		});
	},
	
	onAutoHeight: function (event) {			
		var editor = event.editor,
			wgt = zk.Widget.$(editor.element.getId()),
			cnt = jq('#cke_' + wgt.uuid + '-cnt'),
			body = cnt.find('iframe').contents().find("body"),	
			defaultHeight = zk.parseInt(editor.config.height);
				
		if (wgt._autoHeight) {				
			setTimeout(function(){//body.height() is correct after delay time
				
				var pMargin = zk.parseInt(body.find("P").css("marginBottom")), // for FF
					bodyMargin = zk.parseInt(body.css("marginBottom")),        // for ie
					textArea  = jq(cnt).find('.cke_contents'),
					topHeight = textArea.prev().outerHeight(), // top menu buttons
					bottomHeight = textArea.next().outerHeight(),
					h = body.height() + pMargin + bodyMargin,
					cnth = h + topHeight + bottomHeight;
				
				// Issue 17: autoHeight="true" not work well in ckez 4.0.1.0
				if (cnth < defaultHeight) cnth = defaultHeight;
				h = cnth - topHeight - bottomHeight;
				
				cnt.height(cnth);
				textArea.height(h);
			},20); 
		}
	}
});
