import { Selector } from 'testcafe';

fixture('ZKCK-51')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-51.zul');

test('test', async t => {
	await t
		.click('.z-groupbox-title');

	const ckeditor = Selector('.z-ckeditor'),
		groupbox = Selector('.z-groupbox-content');

	await t.expect(groupbox.clientHeight).gte(await ckeditor.clientHeight);
});