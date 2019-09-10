import { Selector } from 'testcafe';

fixture('ZKCK-50')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-50.zul');

test('Should have onChanging', async t => {
	await t
		.click('input[placeholder="textbox onBlur"]')
		.pressKey('tab')
		.pressKey('H e l l o space w o r l d')

		.expect(Selector('#zk_log').value).contains('[InputEvent onChanging <CKeditor');
});

test('Should have onChanging #2', async t => {
	await t
		.click('input[placeholder="textbox no onBlur"]')
		.pressKey('shift+tab')
		.pressKey('H e l l o space w o r l d')

		.expect(Selector('#zk_log').value).contains('[InputEvent onChanging <CKeditor');
});