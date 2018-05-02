<template>
    <iframe :src="src" frameborder="0" ></iframe>
</template>
<script>
    export default {
        data() {
            return {
                src: this.$global.baseUrl + '/swagger-ui.html'
            }
        }
    }
</script>

<style scoped>
    iframe {
        width: 100%;
        height: 99.2%;
    }
</style>
