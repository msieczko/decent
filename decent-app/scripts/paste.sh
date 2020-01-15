#!/bin/sh


if [ $1 = "sa" ]; then
	argval="0x63FC2aD3d021a4D7e64323529a55a9442C444dA0"
elif [ $1 = "sk" ]; then
	argval="0x5c8b9227cd5065c7e3f6b73826b8b42e198c4497f6688e3085d5ab3a6d520e74"

elif [ $1 = "ca" ]; then
	argval="0xD1D84F0e28D6fedF03c73151f98dF95139700aa7"
elif [ $1 = "ck" ]; then
	argval="0x50c8b3fc81e908501c8cd0a60911633acaca1a567d1be8e769c5ae7007b34b23"

elif [ $1 = "ra" ]; then
	argval="0xd59ca627Af68D29C547B91066297a7c469a7bF72"
elif [ $1 = "rk" ]; then
	argval="0x706618637b8ca922f6290ce1ecd4c31247e9ab75cf0530a0ac95c0332173d7c5"

fi

if [ $2 = "l" ]; then
    device="emulator-5554"
elif [ $2 = "r" ]; then
    device="emulator-5556"
fi

echo $argval
adb -s $device shell input text $argval



# role     #  address                                      key
# sender   #1 0x63FC2aD3d021a4D7e64323529a55a9442C444dA0 - 0x5c8b9227cd5065c7e3f6b73826b8b42e198c4497f6688e3085d5ab3a6d520e74
# courier  #2 0xD1D84F0e28D6fedF03c73151f98dF95139700aa7 - 0x50c8b3fc81e908501c8cd0a60911633acaca1a567d1be8e769c5ae7007b34b23
# receiver #3 0xd59ca627Af68D29C547B91066297a7c469a7bF72 - 0x706618637b8ca922f6290ce1ecd4c31247e9ab75cf0530a0ac95c0332173d7c5
  #4 0xc2FCc7Bcf743153C58Efd44E6E723E9819E9A10A - 0xe217d63f0be63e8d127815c7f26531e649204ab9486b134ec1a0ae9b0fee6bcf
  #5 0x2ad611e02E4F7063F515C8f190E5728719937205 - 0x8101cca52cd2a6d8def002ffa2c606f05e109716522ca2440b2cc84e4d49700b
  #6 0x5e8b3a7e6241CeE1f375924985F9c08706f41d34 - 0x837fd366bc7402b65311de9940de0d6c0ba3125629b8509aebbfb057ebeaaa25
  #7 0xFC6F167a5AB77Fe53C4308a44d6893e8F2E54131 - 0xba35c32f7cbda6a6cedeea5f73ff928d1e41557eddfd457123f6426a43adb1e4
  #8 0xDe41151d0762CB537921c99208c916f1cC7dA04D - 0x71f7818582e55456cb575eea3d0ce408dcf4cbbc3d845e86a7936d2f48f74035