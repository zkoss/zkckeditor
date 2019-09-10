import { Selector } from 'testcafe';

fixture('ZKCK-16')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-16.zul');

test('test', async t => {
	await t
		.click('iframe.cke_wysiwyg_frame')
		.pressKey('H e l l o space w o r l d')
		.click('a.cke_button.cke_button__source')
		.click('.z-button')

		.expect(Selector('.z-messagebox .z-label').innerText).contains('Hello&nbsp;world');
});