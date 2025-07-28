//@version=6
indicator(title="RSI Divergence", format=format.price)

// ——— Inputs ———
len = input.int(title="RSI Period", minval=1, defval=14)
src = input(title="RSI Source", defval=close)
lbR = input(title="Pivot Lookback Right", defval=5)
lbL = input(title="Pivot Lookback Left", defval=5)
rangeUpper = input(title="Max of Lookback Range", defval=60)
rangeLower = input(title="Min of Lookback Range", defval=5)
plotBull = input(true, title="Plot Bullish")
plotHiddenBull = input(false, title="Plot Hidden Bullish")
plotBear = input(true, title="Plot Bearish")
plotHiddenBear = input(false, title="Plot Hidden Bearish")

// ——— Styling ———
bearColor = color.red
bullColor = color.green
hiddenBullColor = color.new(color.green, 80)
hiddenBearColor = color.new(color.red, 80)
textColor = color.white
noneColor = color.new(color.white, 100)

// ——— RSI ———
osc = ta.rsi(src, len)
plot(osc, title="RSI", linewidth=2, color=#2962FF)
hline(50, title="Middle Line", color=#787B86, linestyle=hline.style_dotted)
obLevel = hline(70, title="Overbought", color=#787B86, linestyle=hline.style_dotted)
osLevel = hline(30, title="Oversold", color=#787B86, linestyle=hline.style_dotted)
fill(obLevel, osLevel, title="Background", color=color.rgb(33, 150, 243, 90))

// ——— Pivot Conditions ———
plFound = not na(ta.pivotlow(osc, lbL, lbR))
phFound = not na(ta.pivothigh(osc, lbL, lbR))
_inRange(cond) =>
    bars = ta.barssince(cond)
    rangeLower <= bars and bars <= rangeUpper

// ——— Regular Bullish ———
inRangePl = _inRange(plFound[1])
oscHL = osc[lbR] > ta.valuewhen(plFound, osc[lbR], 1) and inRangePl
priceLL = low[lbR] < ta.valuewhen(plFound, low[lbR], 1)
bullCond = plotBull and priceLL and oscHL and plFound

plot(plFound ? osc[lbR] : na, offset=-lbR, title="Regular Bullish", linewidth=2, color=bullCond ? bullColor : noneColor)
plotshape(bullCond ? osc[lbR] : na, offset=-lbR, title="Bull Label", text="Bull", style=shape.labelup, location=location.absolute, color=bullColor, textcolor=textColor)

// ——— Hidden Bullish ———
oscLL = osc[lbR] < ta.valuewhen(plFound, osc[lbR], 1) and inRangePl
priceHL = low[lbR] > ta.valuewhen(plFound, low[lbR], 1)
hiddenBullCond = plotHiddenBull and priceHL and oscLL and plFound

plot(plFound ? osc[lbR] : na, offset=-lbR, title="Hidden Bullish", linewidth=2, color=hiddenBullCond ? hiddenBullColor : noneColor)
plotshape(hiddenBullCond ? osc[lbR] : na, offset=-lbR, title="Hidden Bull Label", text="H Bull", style=shape.labelup, location=location.absolute, color=bullColor, textcolor=textColor)

// ——— Regular Bearish ———
inRangePh = _inRange(phFound[1])
oscLH = osc[lbR] < ta.valuewhen(phFound, osc[lbR], 1) and inRangePh
priceHH = high[lbR] > ta.valuewhen(phFound, high[lbR], 1)
bearCond = plotBear and priceHH and oscLH and phFound

plot(phFound ? osc[lbR] : na, offset=-lbR, title="Regular Bearish", linewidth=2, color=bearCond ? bearColor : noneColor)
plotshape(bearCond ? osc[lbR] : na, offset=-lbR, title="Bear Label", text="Bear", style=shape.labeldown, location=location.absolute, color=bearColor, textcolor=textColor)

// ——— Hidden Bearish ———
oscHH = osc[lbR] > ta.valuewhen(phFound, osc[lbR], 1) and inRangePh
priceLH = high[lbR] < ta.valuewhen(phFound, high[lbR], 1)
hiddenBearCond = plotHiddenBear and priceLH and oscHH and phFound

plot(phFound ? osc[lbR] : na, offset=-lbR, title="Hidden Bearish", linewidth=2, color=hiddenBearCond ? hiddenBearColor : noneColor)
plotshape(hiddenBearCond ? osc[lbR] : na, offset=-lbR, title="Hidden Bear Label", text="H Bear", style=shape.labeldown, location=location.absolute, color=bearColor, textcolor=textColor)

// ——— Multi-Timeframe RSI Divergence Table with Color ———
var table divTable = table.new(position.top_right, 8, 1, border_width=1)

getDivergence(tf) =>
    rsi_tf = request.security(syminfo.tickerid, tf, ta.rsi(src, len))
    pl_tf_cond = request.security(syminfo.tickerid, tf, not na(ta.pivotlow(rsi_tf, lbL, lbR)))
    ph_tf_cond = request.security(syminfo.tickerid, tf, not na(ta.pivothigh(rsi_tf, lbL, lbR)))
    oscHL_tf = rsi_tf[lbR] > ta.valuewhen(pl_tf_cond, rsi_tf[lbR], 1)
    priceLL_tf = low[lbR] < ta.valuewhen(pl_tf_cond, low[lbR], 1)
    bullish_tf = oscHL_tf and priceLL_tf and pl_tf_cond
    oscLH_tf = rsi_tf[lbR] < ta.valuewhen(ph_tf_cond, rsi_tf[lbR], 1)
    priceHH_tf = high[lbR] > ta.valuewhen(ph_tf_cond, high[lbR], 1)
    bearish_tf = oscLH_tf and priceHH_tf and ph_tf_cond
    [bullish_tf, bearish_tf]

getLabelAndColor(tf) =>
    [bullish, bearish] = getDivergence(tf)
    label = bullish ? "Bullish" : bearish ? "Bearish" : "None"
    bgcolor = bullish ? color.new(color.green, 0) : bearish ? color.new(color.red, 0) : color.new(color.gray, 80)
    [label, bgcolor]

table.cell(divTable, 0, 0, "Timeframe Status", text_color=color.white, bgcolor=color.gray)

[label1, color1] = getLabelAndColor("1")
[label2, color2] = getLabelAndColor("5")
[label3, color3] = getLabelAndColor("15")
[label4, color4] = getLabelAndColor("30")
[label5, color5] = getLabelAndColor("60")
[label6, color6] = getLabelAndColor("240")
[label7, color7] = getLabelAndColor("D")

table.cell(divTable, 1, 0, "1m: " + label1, text_color=color.white, bgcolor=color1)
table.cell(divTable, 2, 0, "5m: " + label2, text_color=color.white, bgcolor=color2)
table.cell(divTable, 3, 0, "15m: " + label3, text_color=color.white, bgcolor=color3)
table.cell(divTable, 4, 0, "30m: " + label4, text_color=color.white, bgcolor=color4)
table.cell(divTable, 5, 0, "1H: " + label5, text_color=color.white, bgcolor=color5)
table.cell(divTable, 6, 0, "4H: " + label6, text_color=color.white, bgcolor=color6)
table.cell(divTable, 7, 0, "1D: " + label7, text_color=color.white, bgcolor=color7)