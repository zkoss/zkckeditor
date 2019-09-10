import { Selector } from 'testcafe';

fixture('ZKCK-13')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-13.zul');

test('test', async t => {
	await t
		.typeText('.z-textbox', '<abc>ddd</abc>')
		.click('.z-button')
		.switchToIframe('iframe.cke_wysiwyg_frame')

		.expect(Selector('body').innerText).eql('ddd');
});