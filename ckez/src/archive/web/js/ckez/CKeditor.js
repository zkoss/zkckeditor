/* CKeditor.js

	Purpose:

	Description:

	History:
		Tue Oct 7 17:56:45     2009, Created by jimmyshiau

Copyright (C) 2009 Potix Corporation. All Rights Reserved.

This program is distributed under LGPL Version 2.1 in the hope that
it will be useful, but WITHOUT ANY WARRANTY.
*/
CKEDITOR.focusManager._.blurDelay = 0;

ckez.CKeditor = zk.$extends(zul.Widget, {
	_value: '',
	
	$define: {
		value: [function(v) {
			return !v ? '' : v;
		}, function (v, fromServer) {
			var editor = this.getEditor();
			if (editor) {
				editor.setData(v);
				// Issue #9: update editor's previousValue if set value from server
				// to prevent unexpect onChange event
				if (fromServer)
					editor._.previousValue = editor.dataProcessor.toHtml(v);
			}
		}],
		autoHeight: null,
		customConfigurationsPath: _zkf = function () {
			if (this.desktop)
				this.rerender();
		},
		config: _zkf,
		toolbar: _zkf,
		toolbar: _zkf,
		width: function (v) {
			this._syncSize();
		},
		height: function (v) {
			this._syncSize();
		},
		/**
		 * Returns whether enable to resize of the component or not.
		 * <p>Default: true.
		 * @since 4.16.1.1
		 */
		/**
		 * Sets whether enable to resize of the component or not.
		 * @since 4.16.1.1
		 */
		resizable: function (resizable) {
			this._contentResizable(resizable);
		}
	},
	setFlexSize_: function(sz, ignoreMargins) {
		this.$supers('setFlexSize_', arguments);
		// ZKCK-37 When maximized, don't do anything. Let CKEditor handle it.
		if (this._isMaximize)
			return;
		this._syncSize();
	},

	redraw: function (out) {
		out.push('<div', this.domAttrs_(), '><textarea id="', this.uuid, '-cnt"></textarea></div>');
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
		zWatch.listen({
			onSend : this,
			onRestore : this,
			onVParent : this,
			onShow: this
		});
	},
	
	unbind_ : function() {
		this._unbind = true;
		if (!this._editor) {
			// ensure to destroy failed-initialized editor
			this._editor = CKEDITOR.instances[this.uuid + '-cnt'];
		}
		if (this._editor) {//bug 3048386: detach ckeditor before it loaded cause js error
			// Issue 18, 19: If it catches js error when destroying ckeditor, then finishes the following instructions
			try {
				this._editor.destroy(true);
			} catch (err) {
				// finish detaching ckeditor
				CKEDITOR.tools.removeFunction(this._editor._.frameLoadedHandler);
				this._editor.fire('contentDomUnload');

				// finish destroying ckeditor
				this._editor.status = 'destroyed';
				this._editor.fire('destroy');
				this._editor.removeAllListeners();
				CKEDITOR.remove(this._editor);
				CKEDITOR.fire('instanceDestroyed', null, this._editor);
			}
		}

		this._editor = this._focusManager = null;
		ckez.CKeditor._toggleOnResize(true, this); // remember to register resize back
		zWatch.unlisten({
			onSend : this,
			onRestore : this,
			onVParent : this,
			onShow: this
		});
		this.$supers('unbind_', arguments);
		this._unbind = null;
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
		this._restore();
	},
	
	// Prevent uneditable problem when makeVParent
	onVParent: function (evt) {
		// B-ZKCK-8: call restore only if editor is a child of origin
		if (this._isChildOf(evt.origin)) {
			this._restore();
		}
	},

	onShow: function () {
		var editor = this._editor;
		if (editor) {
			jq(this).find('.cke_contents').height(editor.config.height);
			this._syncSize();
		}
	},
	
	_isChildOf: function(wgt) {
		var p = this.parent;
		if (p) {
			do {
				if (p == wgt)
					return true;
				p = p.parent;
			} while (p != wgt.desktop);
		}
		return false;
	},
	
	_restore: function () {
		var iframe = jq('#cke_' + this.uuid + '-cnt iframe')[0];
		if (!iframe) return;

		if (this._editor)
			CKEDITOR.remove( this._editor );

		jq(this.$n()).html('<textarea id="' + this.uuid + '-cnt">' + this._value + '</textarea>');
		this.clearCache();
		this._init();
		
		if (zk.ie)
			jq('#cke_' + this.uuid + '-cnt').width(jq('#cke_'+this.uuid+'-cnt').width());
	},

	_syncSize: function () {
		var editor = this._editor,
			n = this.$n(),
			tmp;
		// Restore the fix for ZKCK-37 (but moved from `onSize` to `_syncSize` here) that was removed in
		// https://github.com/zkoss/zkckeditor/commit/df9697fa81bcdad0222db8d2570f9fa551b238a1
		// The issue was re-introduced in release 4.16.2.0 whereas release 4.16.1.1 was not plagued.
		if (n && editor && (tmp = editor.document) && (tmp = tmp.getWindow()) && tmp.$) {
			editor.resize('100%', n.clientHeight);
			jq(n).css("overflow", "auto");
		}
	},

	_contentResizable: function (resizable) {
		var editor = this._editor;
		if (editor) {
			editor.config.resize_enabled = resizable;
			this.rerender();
		}
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
		// ZKCK-40: detached before init
		if (!this.desktop || this._unbind) return;
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
				customConfig: customConfigPath
			});
		
		if (this._config) {
			zk.override(config, {}, this._config);
		}
		
		if (filebrowserBrowseUrl)
			config.filebrowserBrowseUrl = fileBrowserTempl + '?Type=Files&dtid=' + dtid + '&uuid=' + uuid;

		if (filebrowserImageBrowseUrl)
			config.filebrowserImageBrowseUrl = fileBrowserTempl + '?Type=Images&dtid=' + dtid + '&uuid=' + uuid;

		if (filebrowserFlashBrowseUrl)
			config.filebrowserFlashBrowseUrl = fileBrowserTempl + '?Type=Flash&dtid=' + dtid + '&uuid=' + uuid;
		
		if (filebrowserUploadUrl)
			config.filebrowserUploadUrl = fileUploadTempl + '?Type=Files&dtid=' + dtid + '&uuid=' + uuid;

		if (filebrowserImageUploadUrl)
			config.filebrowserImageUploadUrl = fileUploadTempl + '?Type=Images&dtid=' + dtid + '&uuid=' + uuid;

		if (filebrowserFlashUploadUrl)
			config.filebrowserFlashUploadUrl = fileUploadTempl + '?Type=Flash&dtid=' + dtid + '&uuid=' + uuid;
		
		if (this._toolbar)
			config.toolbar = this._toolbar;
		
		if (!this.isResizable())
			config.resize_enabled = false;
		
		var cnt = this.$n('cnt');
		jq(cnt).text(this._value); // ZKCK-13: init the value of the textarea here instead of at redraw
		
		jq(cnt).ckeditor(function(){
			if (wgt._unbind) {
				this.destroy();
				return;
			}
			wgt._editor = this;
			wgt._focusManager = new CKEDITOR.focusManager(this);
			this.on('focus', ckez.CKeditor.onFocus);
			this.on('blur', ckez.CKeditor.onBlur);
			this.on('selectionChange', ckez.CKeditor.onSelection);
			wgt._overrideFormSubmit();
			this.on('key', ckez.CKeditor.onAutoHeight); //on press any key
			this.on('loadSnapshot', ckez.CKeditor.onAutoHeight);//on Redo And Undo
			this.on('beforePaste', ckez.CKeditor.onAutoHeight);
			this.on('maximize', ckez.CKeditor.onMaximize);
			// ZKCK-30
			jq('iframe', this.container.$).contents().click(wgt.proxy(wgt._mimicOnClick));
			this.resetDirty();

			wgt._syncSize();
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
	},

	_mimicOnClick: function (event) {
		zWatch.fire('onFloatUp', this, {triggerByClick: event.which});
	}
}, {
	onFocus: function (event) {
		var editor = event.editor,
			wgt = zk.Widget.$(editor.element.getId()),
			tmp = editor._.previousValue;
		// ZKCK-30
		zWatch.fire('onFloatUp', wgt, {triggerByFocus: true});

		if (!wgt._tidChg)
			wgt._tidChg = setInterval(function () {
				if (tmp != editor._.previousValue)
					tmp = wgt.previousValue = editor._.previousValue;

				// Issue #7
				if (editor.checkDirty() && wgt.previousValue != editor.getData()) {
					wgt.fire('onChanging', {
						value: editor.getData(),
						start: 0,
						bySelectBack: false
						},
					{ignorable:1}, 100);

					if (editor.checkDirty()) // Issue #7
						wgt.previousValue = editor.getData();
				}
			}, 500);
	},
	
	onBlur: function (event, ahead) {
		var editor = event.editor || event,
			wgt = zk.Widget.$(editor.element.getId());
		
		// clicking some toolbar buttons will also trigger onBlur
		// but the focus is considered still "inside" ckeditor widget
		// if the editor instance did not lose focus, ignore the onBlur event
		if (zk.currentFocus === wgt) return;

		// ZKCK-50: If CKEditor has the focus, just ignore it
		if (wgt._tidChg && !wgt._focusManager.hasFocus) {
			clearInterval(wgt._tidChg);
			wgt._tidChg = null;
		}
		
		if (!editor.document)
			editor.document = editor.element.getDocument();
		
		if (wgt.$class._checkEditorDirty(editor)) { // Issue #13
			var val = editor.getData();
			wgt._value = val; //save for onRestore
			//ZKCK-16, 17 use sendAhead to make sure onChange always happens first
			wgt.fire('onChange', {value: val}, {sendAhead: ahead});
			editor.resetDirty();
		}
	},
	
	onSelection: function (event) {
	},
	
	onAutoHeight: function (event) {
		var editor = event.editor,
			wgt = zk.Widget.$(editor.element.getId()),
			cnt = jq('#cke_' + wgt.uuid + '-cnt'),
			body = cnt.find('iframe').contents().find("body"),
			defaultHeight = zk.parseInt(editor.config.height);
				
		if (wgt._autoHeight) {
			setTimeout(function(){ //body.height() is correct after delay time
				
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

				textArea.height(h);
			},20);
		}
	},

	onMaximize: function (event) {
		var editor = event.editor,
			wgt = zk.Widget.$(editor.element.getId()),
			isMaximize = event.data == CKEDITOR.TRISTATE_ON;
		wgt._isMaximize = isMaximize;
		ckez.CKeditor._toggleOnResize(!isMaximize, wgt);
		if (!isMaximize)
			zWatch.fire('onSize');
	},

	_toggleOnResize: function (enabled, wgt) {
		var jqWindow = jq(window),
			resizeHandlers;
		if (enabled) {
			resizeHandlers = wgt._resizeHandlers;
			if (resizeHandlers) {
				for (var i = 0, len = resizeHandlers.length; i < len; i++) {
					jqWindow.resize(resizeHandlers[i].handler);
				}
			}
			wgt._resizeHandlers = null;
		} else {
			resizeHandlers = this._getJQueryEventHandlers()['resize'];
			if (resizeHandlers)
				resizeHandlers = resizeHandlers.slice(); // shallow copy from a live array
			wgt._resizeHandlers = resizeHandlers;
			jqWindow.unbind('resize');
		}
	},
	_getJQueryEventHandlers: function () {
		if (typeof(jq._data) === 'function') { // jQuery 1.8+
			return jq._data(window, 'events') || {};
		}
		var jqWindow = jq(window);
		if (typeof(jqWindow.data) === 'function') { // jQuery <= 1.7
			return jqWindow.data('events') || {};
		}
		return {};
	},
	
	// Issue #13: pass through the html formatter before compare
	_checkEditorDirty: function (editor) {
		var fmtSnapshot = editor.dataProcessor.toHtml(editor.getSnapshot());
		var fmtPreviousVal = editor.dataProcessor.toHtml(editor._.previousValue);
		return editor.status == 'ready' && fmtPreviousVal !== fmtSnapshot;
	}
});
