/**
 * Created by dell on 2017-11-29.
 */
import * as types from '../mutation-types'

// initial state
const state = {
    curPage: 1,
    pageSize: 10,
    fbName: '',
    username: '',
    choseTime: []
};

// getters
const getters = {
    instoreCurPage: state => state.curPage,
    instorePageSize: state => state.pageSize,
    instoreFbName: state => state.fbName,
    instoreUsername: state => state.username,
    instoreChoseTime: state => state.choseTime
};

// mutations
const mutations = {
    // 我们可以使用 ES2015 风格的计算属性命名功能来使用一个常量作为函数名
    [types.INSTORE_CUR_PAGE](state, curPage){
        state.curPage = curPage;
    },
    [types.INSTORE_PAGE_SIZE](state, pageSize){
        state.pageSize = pageSize;
    },
    [types.INSTORE_FB_NAME](state, fbName){
        state.fbName = fbName;
    },
    [types.INSTORE_USERNAME](state, username){
        state.username = username;
    },
    [types.INSTORE_CHOSE_TIME](state, choseTime){
        state.choseTime = choseTime;
    }
};

// actions
const actions = {
    setInstoreCurPage ({commit}, curPage) {
        commit(types.INSTORE_CUR_PAGE, curPage)
    },
    setInstorePageSize ({commit}, pageSize) {
        commit(types.INSTORE_PAGE_SIZE, pageSize)
    },
    setInstoreFbName ({commit}, fbName) {
        commit(types.INSTORE_FB_NAME, fbName)
    },
    setInstoreUsername ({commit}, username) {
        commit(types.INSTORE_USERNAME, username)
    },
    setInstoreChoseTime ({commit}, choseTime) {
        commit(types.INSTORE_CHOSE_TIME, choseTime)
    }
};

export default {
    state,
    getters,
    mutations,
    actions
}
