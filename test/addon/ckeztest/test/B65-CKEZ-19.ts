import { Selector } from 'testcafe';
import checkConsoleError from './check-console-error';

fixture('B65-CKEZ-19')
	.page('http://localhost:8080/ckeztest/test2/B65-CKEZ-19.zul');

test('test', async t => {
	await t
		.click('button')
		.click('.z-window-close');
	await checkConsoleError();
});