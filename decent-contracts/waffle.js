const fs = require('fs');

const getConfig = () => {
  fs.mkdirSync("build/contracts", {recursive: true});
  return ({
    sourcesPath: "src",
    targetPath: "build/contracts",
    compiler: "solcjs",
    solcVersion: "v0.5.7+commit.6da8b019",
  });
};

module.exports = getConfig();
