import { Selector } from 'testcafe';

fixture('ZKCK-46')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-46.zul');

test('test', async t => {
	await t
		.click('.cke_button__table')
		.click('.cke_dialog_ui_button_ok');

	await t
		.switchToIframe('.cke_wysiwyg_frame')
		.rightClick('td')
		.switchToMainWindow()
		.switchToIframe('.cke_menu_panel iframe')
		.click('.cke_menubutton__tablecolumn')
		.switchToMainWindow()
		.switchToIframe(Selector('.cke_menu_panel iframe').nth(-1))
		.click('.cke_menubutton__tablecolumn_insertBefore');

	await t
		.switchToMainWindow()
		.switchToIframe('.cke_wysiwyg_frame')
		.rightClick('td.cke_table-faked-selection')
		.switchToMainWindow()
		.switchToIframe('.cke_menu_panel iframe')
		.click('.cke_menubutton__tablerow')
		.switchToMainWindow()
		.switchToIframe(Selector('.cke_menu_panel iframe').nth(-1))
		.click('.cke_menubutton__tablerow_insertBefore')
		.switchToMainWindow();
});