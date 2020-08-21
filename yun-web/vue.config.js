const path = require('path')
const defaultSettings = require('./src/settings.js')

function resolve (dir) {
  return path.join(__dirname, dir)
}

const name = defaultSettings.title // 网址标题
// All configuration item explanations can be find in https://cli.vuejs.org/config/
module.exports = {
  configureWebpack: {
    // provide the app's title in webpack's name field, so that
    // it can be accessed in index.html to inject the correct title.
    name: name,
    resolve: {
      alias: {
        '@': resolve('src')
      }
    }
  }
}
