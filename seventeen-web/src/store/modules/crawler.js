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
    status: '0',
    choseTime: [],
    currentTag: [],
    tags: [],
};

// getters
const getters = {
    curPage: state => state.curPage,
    pageSize: state => state.pageSize,
    fbName: state => state.fbName,
    username: state => state.username,
    status: state => state.status,
    choseTime: state => state.choseTime,
    currentTag: state => state.currentTag,
    tags: state => state.tags,
};

// mutations
const mutations = {
    // 我们可以使用 ES2015 风格的计算属性命名功能来使用一个常量作为函数名
    [types.CRAWLER_CUR_PAGE](state, curPage){
        state.curPage = curPage;
    },
    [types.CRAWLER_PAGE_SIZE](state, pageSize){
        state.pageSize = pageSize;
    },
    [types.CRAWLER_FB_NAME](state, fbName){
        state.fbName = fbName;
    },
    [types.CRAWLER_USERNAME](state, username){
        state.username = username;
    },
    [types.CRAWLER_STATUS](state, status){
        state.status = status;
    },
    [types.CRAWLER_CHOSE_TIME](state, choseTime){
        state.choseTime = choseTime;
    },
    [types.CURRENT_TAG](state, currentTag){
        state.currentTag = currentTag;
    },
    [types.TAGS](state, tags){
        state.tags = tags;
    }
};

// actions
const actions = {
    setCurPage ({commit}, curPage) {
        commit(types.CRAWLER_CUR_PAGE, curPage)
    },
    setPageSize ({commit}, pageSize) {
        commit(types.CRAWLER_PAGE_SIZE, pageSize)
    },
    setFbName ({commit}, fbName) {
        commit(types.CRAWLER_FB_NAME, fbName)
    },
    setUsername ({commit}, username) {
        commit(types.CRAWLER_USERNAME, username)
    },
    setStatus ({commit}, status) {
        commit(types.CRAWLER_STATUS, status)
    },
    setChoseTime ({commit}, choseTime) {
        commit(types.CRAWLER_CHOSE_TIME, choseTime)
    },
    setCurrentTag ({commit}, currentTag) {
        commit(types.CURRENT_TAG, currentTag)
    },
    setTags ({commit}, tags) {
        commit(types.TAGS, tags)
    },
};

export default {
    state,
    getters,
    mutations,
    actions
}
