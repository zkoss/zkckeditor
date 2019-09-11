import { Selector } from 'testcafe';

fixture('ZKCK-48')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-48.zul');

test('WCS dialog should be closed after finishing', async t => {
	await t
		.click('.cke_wysiwyg_frame')
		.pressKey('a b c d e f g')
		.click('.cke_button__spellchecker')
		.click('.cke_dialog_ui_input_text')
		.click('.cke_dialog_ui_button[title-cmd="FinishChecking"]')

		.expect(Selector('#overlayBlock').visible).notOk();
});