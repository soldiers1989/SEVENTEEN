/**
 * Created by dell on 2017-11-29.
 */
import Vue from 'vue'
import Vuex from 'vuex'
import crawler from './modules/crawler'
import instore from './modules/instore'

Vue.use(Vuex);

const store = new Vuex.Store({
    modules: {
        crawler,
        instore,
    },
    strict: false
});
export default store;
