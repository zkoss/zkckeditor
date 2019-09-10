import { Selector } from 'testcafe';
import checkConsoleError from './check-console-error';

fixture('B65-CKEZ-20')
	.page('http://localhost:8080/ckeztest/test2/B65-CKEZ-20.zul');

test('test', async t => {
	await t
		.click('iframe.cke_wysiwyg_frame')
		.pressKey('T E S T')
		.switchToIframe('iframe.cke_wysiwyg_frame')
		.selectEditableContent('body', 'body')
		.switchToMainWindow()
		.click('a[title="Font Size"]')
		.switchToIframe('.cke_combopanel__fontsize iframe')
		.click('a[title="8"]')
		.switchToMainWindow()
		.click('.z-button');
	const label = Selector('.z-label').nth(-1);
	await t.expect(label.innerText).contains('<span style="font-size:8px">TEST</span>');
});