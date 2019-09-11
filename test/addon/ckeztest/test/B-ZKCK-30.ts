import { Selector } from 'testcafe';

fixture('ZKCK-30')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-30.zul');

test('Popup should be hidden by mouse click', async t => {
	await t
		.click('.z-button')
		.switchToIframe('.cke_wysiwyg_frame')
		.click('body');
	await t
		.switchToMainWindow()
		.expect(Selector('.z-popup').visible).notOk();
});

test('Notification should be hidden by mouse click', async t => {
	await t
		.click('.z-datebox-button')
		.switchToIframe('.cke_wysiwyg_frame')
		.click('body');
	await t
		.switchToMainWindow()
		.expect(Selector('.z-datebox-popup').visible).notOk();
});

test('Popup should be remained by Tab', async t => {
	await t
		.click('.z-button')
		.pressKey('tab tab h e l l o');
	await t
		.expect(Selector('.z-popup').visible).ok();
});