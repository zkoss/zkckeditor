import { Selector } from 'testcafe';

fixture('ZKCK-48')
	// According to CKeditor 4.18.0 release note, Web Spell Checker ended support for WebSpellChecker Dialog on
	// December 31st, 2021. This means the plugin is not supported any longer. Therefore, we decided to deprecate
	// and remove the WebSpellChecker Dialog plugin from CKEditor 4 presets.
	.skip
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-48.zul');

// WCS dialog should be closed after finishing
test('Skipped, no need to test, as WCS has been deprecated.', async t => {
	await t
		.click('.cke_wysiwyg_frame')
		.pressKey('a b c d e f g')
		.click('.cke_button__spellchecker')
		.click('.cke_dialog_ui_input_text')
		.click('.cke_dialog_ui_button[title-cmd="FinishChecking"]')

		.expect(Selector('#overlayBlock').visible).notOk();
});