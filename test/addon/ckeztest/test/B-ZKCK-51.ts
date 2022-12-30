import { Selector } from 'testcafe';
import * as assert from 'assert';

fixture('ZKCK-51')
	.page('http://localhost:8080/ckeztest/test2/B-ZKCK-51.zul');

test('test', async t => {
	await t
		.click('.z-groupbox-title');

	// https://testcafe.io/documentation/402670/reference/test-api/domnodestate
	const ckeditor = await Selector('.z-ckeditor')(),
		groupbox = await Selector('.z-groupbox-content')();
	assert.ok(ckeditor.clientHeight, '`clientHeight` of ".z-ckeditor" is undefined');
	assert.ok(groupbox.clientHeight, '`clientHeight` of ".z-groupbox-content" is undefined');
	assert.ok(groupbox.clientHeight >= ckeditor.clientHeight);
});