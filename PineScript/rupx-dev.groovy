//@version=6
indicator("Rupx-Dev Pro", overlay=true, max_lines_count=500, max_labels_count=500)

// ———— Color Scheme ————
colorSuperFast    = #00FFFF    // Cyan
colorFast         = #00FF00    // Lime Green
colorNormal       = #FF00FF    // Magenta
colorLazy         = #FFA500    // Orange
colorNormal1H     = #4B0082    // Indigo
colorLazy1H       = #8A2BE2    // Blue Violet
colorNormal4H     = #1E90FF    // Dodger Blue
colorLazy4H       = #87CEEB    // Sky Blue
colorBB           = #FFFFFF    // White
colorBB1H         = #FFD700    // Gold
colorBB4H         = #00FF00    // Green
colorBB1D         = #FFA07A  // Light Salmon
colorText         = color.white
colorBG           = #131722    // Dark Background

// ———— Set Background ————
bgcolor(colorBG)

// ———— EMA Configuration ————
ShowSuperFastEMA = input(true, "Show SuperFast EMA", group="EMA Settings")
ShowFastEMA = input(true, "Show Fast EMA", group="EMA Settings")
ShowNormalEMA = input(true, "Show Normal EMA", group="EMA Settings")
ShowLazyEMA = input(true, "Show Lazy EMA", group="EMA Settings")
Show1HNormalEMA = input(true, "Show 1H Normal EMA", group="EMA Settings")
Show1HLazyEMA = input(true, "Show 1H Lazy EMA", group="EMA Settings")
Show4HNormalEMA = input(true, "Show 4H Normal EMA", group="EMA Settings")
Show4HLazyEMA = input(true, "Show 4H Lazy EMA", group="EMA Settings")

SuperFastEMA = input(5, "SuperFast Period", group="EMA Settings")
FastEMA = input(9, "Fast Period", group="EMA Settings")
NormalEMA = input(50, "Normal Period", group="EMA Settings")
LazyEMA = input(200, "Lazy Period", group="EMA Settings")

// ———— EMA Calculations ————
SUPERFASTEMA = ta.ema(close, SuperFastEMA)
FASTEMA = ta.ema(close, FastEMA)
NORMALEMA = ta.ema(close, NormalEMA)
LAZYEMA = ta.ema(close, LazyEMA)

// ———— Multi-Timeframe EMAs ————
NormalEMA1H = request.security(syminfo.tickerid, "60", ta.ema(close, NormalEMA), lookahead=barmerge.lookahead_off)
LazyEMA1H = request.security(syminfo.tickerid, "60", ta.ema(close, LazyEMA), lookahead=barmerge.lookahead_off)
NormalEMA4H = request.security(syminfo.tickerid, "240", ta.ema(close, NormalEMA), lookahead=barmerge.lookahead_off)
LazyEMA4H = request.security(syminfo.tickerid, "240", ta.ema(close, LazyEMA), lookahead=barmerge.lookahead_off)

// ———— EMA Plots ————
plot(ShowSuperFastEMA ? SUPERFASTEMA : na, "SuperFast", color.new(colorSuperFast, 40), 2)
plot(ShowFastEMA ? FASTEMA : na, "Fast", color.new(colorFast, 40), 2)
plot(ShowNormalEMA ? NORMALEMA : na, "Normal", color.new(colorNormal, 40), 2)
plot(ShowLazyEMA ? LAZYEMA : na, "Lazy", color.new(colorLazy, 40), 2)
plot(Show1HNormalEMA ? NormalEMA1H : na, "1H Normal", color.new(colorNormal1H, 60), 2)
plot(Show1HLazyEMA ? LazyEMA1H : na, "1H Lazy", color.new(colorLazy1H, 60), 2)
plot(Show4HNormalEMA ? NormalEMA4H : na, "4H Normal", color.new(colorNormal4H, 80), 2)
plot(Show4HLazyEMA ? LazyEMA4H : na, "4H Lazy", color.new(colorLazy4H, 80), 2)

// ———— Bollinger Bands Configuration ————
bbLength = input.int(20, "BB Length", group="Bollinger Bands")
bbSource = input.source(close, "BB Source", group="Bollinger Bands")
bbMultiplier = input.float(2.0, "BB Multiplier", group="Bollinger Bands")

// ———— Current TF BB ————
basis = ta.sma(bbSource, bbLength)
dev = bbMultiplier * ta.stdev(bbSource, bbLength)
upperBB = basis + dev
lowerBB = basis - dev

// ———— 1H BB ————
[h1Basis, h1Dev] = request.security(syminfo.tickerid, "60", 
     [ta.sma(bbSource, bbLength), bbMultiplier * ta.stdev(bbSource, bbLength)])
h1Upper = h1Basis + h1Dev
h1Lower = h1Basis - h1Dev

// ———— 4H BB ————
[h4Basis, h4Dev] = request.security(syminfo.tickerid, "240", 
     [ta.sma(bbSource, bbLength), bbMultiplier * ta.stdev(bbSource, bbLength)])
h4Upper = h4Basis + h4Dev
h4Lower = h4Basis - h4Dev

// ———— 1D BB ————
[d1Basis, d1Dev] = request.security(syminfo.tickerid, "D", 
     [ta.sma(bbSource, bbLength), bbMultiplier * ta.stdev(bbSource, bbLength)])
d1Upper = d1Basis + d1Dev
d1Lower = d1Basis - d1Dev

// ———— Plot Bollinger Bands ————
plot(upperBB, "Upper BB", color.new(colorBB, 30), 1)
plot(basis, "Mid BB", color.new(colorBB, 30), 1)
plot(lowerBB, "Lower BB", color.new(colorBB, 30), 1)

plot(h1Upper, "1H Upper", color.new(colorBB1H, 50), 1)
plot(h1Basis, "1H Mid", color.new(colorBB1H, 50), 1)
plot(h1Lower, "1H Lower", color.new(colorBB1H, 50), 1)

plot(h4Upper, "4H Upper", color.new(colorBB4H, 70), 1)
plot(h4Basis, "4H Mid", color.new(colorBB4H, 70), 1)
plot(h4Lower, "4H Lower", color.new(colorBB4H, 70), 1)

plot(d1Upper, "1D Upper", color.new(colorBB1D, 70), 1)
plot(d1Basis, "1D Mid", color.new(colorBB1D, 70), 1)
plot(d1Lower, "1D Lower", color.new(colorBB1D, 70), 1)

// ———— Alert System ————
alertBullish = input(true, "Enable Bullish Alerts", group="Alerts")
alertBearish = input(true, "Enable Bearish Alerts", group="Alerts")

// Golden/Death Cross
goldenCross = ta.crossover(NORMALEMA, LAZYEMA)
deathCross = ta.crossunder(NORMALEMA, LAZYEMA)

alertcondition(goldenCross and alertBullish, "Golden Cross", "Bullish Trend: 50EMA crossed above 200EMA")
alertcondition(deathCross and alertBearish, "Death Cross", "Bearish Trend: 50EMA crossed below 200EMA")

// BB Breakout
bbUpperBreak = close > upperBB
bbLowerBreak = close < lowerBB
alertcondition(bbUpperBreak and alertBullish, "BB Upper Break", "Price broke above Upper Bollinger Band")
alertcondition(bbLowerBreak and alertBearish, "BB Lower Break", "Price broke below Lower Bollinger Band")



// Table

// Input Parameters
rsiLength = input.int(14, title="RSI Length")
rsiOverbought = input.int(70, title="RSI Overbought Level")
rsiOversold = input.int(30, title="RSI Oversold Level")
ShowTable = input.bool(true, title="Show Table for Multi-Timeframe")

// Calculate RSI
rsiValue = ta.rsi(close, rsiLength)

// RSI Overbought/Oversold Conditions
rsiOverboughtCondition = rsiValue > rsiOverbought
rsiOversoldCondition = rsiValue < rsiOversold

// Optional: Add background color to RSI pane for better visualization
bgcolor(rsiOverboughtCondition ? color.new(color.red, 90) : na, title="RSI Overbought Background")
bgcolor(rsiOversoldCondition ? color.new(color.green, 90) : na, title="RSI Oversold Background")


// Define EMA values
EMA50 = ta.ema(close, 50)
EMA200 = ta.ema(close, 200)

// Helper function to format the text and add bull/bear conditions
gettext(bull, bear, iv) =>
    bull ? str.tostring(iv, format.mintick) : bear ? str.tostring(iv, format.mintick) : str.tostring(iv, format.mintick)

// Modify getcolor function to return green for bull, red for bear, and dark for neutral
getcolor(bull, bear) =>
    bull ? color.green : bear ? color.red : color.rgb(0, 0, 0, 90) // Dark background for neutral, green for bull, red for bear

// Function to add cells to the table
addcell(tableId, row, ind, bull, bear, iv) =>
    [bu1m, be1m, iv1m] = request.security(syminfo.tickerid, "1", [bull, bear, iv])
    [bu5, be5, iv5] = request.security(syminfo.tickerid, "5", [bull, bear, iv])
    [bu15, be15, iv15] = request.security(syminfo.tickerid, "15", [bull, bear, iv])
    [bu1h, be1h, iv1h] = request.security(syminfo.tickerid, "60", [bull, bear, iv])
    [bu4h, be4h, iv4h] = request.security(syminfo.tickerid, "240", [bull, bear, iv])
    [bu1d, be1d, iv1d] = request.security(syminfo.tickerid, "D", [bull, bear, iv])

    
    tableId.cell(0, row, ind, text_color=color.white)
    tableId.cell(1, row, gettext(bu1m, be1m, iv1m), bgcolor=getcolor(bu1m, be1m), text_color=color.white)
    tableId.cell(2, row, gettext(bu5, be5, iv5), bgcolor=getcolor(bu5, be5), text_color=color.white)
    tableId.cell(3, row, gettext(bu15, be15, iv15), bgcolor=getcolor(bu15, be15), text_color=color.white)
    tableId.cell(4, row, gettext(bu1h, be1h, iv1h), bgcolor=getcolor(bu1h, be1h), text_color=color.white)
    tableId.cell(5, row, gettext(bu4h, be4h, iv4h), bgcolor=getcolor(bu4h, be4h), text_color=color.white)
    tableId.cell(6, row, gettext(bu1d, be1d, iv1d), bgcolor=getcolor(bu1d, be1d), text_color=color.white)


// If ShowTable is enabled, create the table and add cells
if ShowTable
    // Create a table with dark background color
    tblRulesRsiEmas = table.new(position.top_right, 10, 12, bgcolor=color.rgb(0, 0, 0, 90), border_color=color.navy, border_width=2)

    // Table headers with dark background and white text
    tblRulesRsiEmas.cell(0, 0, "IND", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRulesRsiEmas.cell(1, 0, "1M", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRulesRsiEmas.cell(2, 0, "5M", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRulesRsiEmas.cell(3, 0, "15M", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRulesRsiEmas.cell(4, 0, "1H", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRulesRsiEmas.cell(5, 0, "4H", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRulesRsiEmas.cell(6, 0, "1D", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)


    // Check RSI bullish or bearish conditions
    isBullish = rsiValue > 60
    isBearish = rsiValue < 40

    // Add cells for RSI, EMA50, and EMA200
    addcell(tblRulesRsiEmas, 1, "RSI", isBullish, isBearish, rsiValue)
    addcell(tblRulesRsiEmas, 3, "EMA50", EMA50 > close - (close * 1 / 1000) and EMA50 < (close + (close * 1 / 100)), EMA50 > close - (close * 2 / 100) and EMA50 < (close + (close * 2 / 1000)), EMA50)
    addcell(tblRulesRsiEmas, 4, "EMA200", EMA200 > close - (close * 1 / 1000) and EMA200 < (close + (close * 1 / 1000)), EMA200 > close - (close * 5 / 1000) and EMA200 < (close + (close * 5 / 1000)), EMA200)



// ———— Color Legend Table ————
ShowColorLegend = input(true, "Show Color Legend", group="Color Legend Settings")

if ShowColorLegend
    // Create table in bottom right corner
    var tblColorLegend = table.new(position.bottom_right, 2, 23, bgcolor=color.rgb(19, 23, 34, 95), border_width=1)
    
    // Table headers
    table.cell(tblColorLegend, 0, 0, "Indicator", text_color=color.white, bgcolor=color.rgb(19, 23, 34))
    table.cell(tblColorLegend, 1, 0, "Color", text_color=color.white, bgcolor=color.rgb(19, 23, 34))
    
    // EMA Color Legend
    table.cell(tblColorLegend, 0, 1, "5 EMA", text_color=color.white)
    table.cell(tblColorLegend, 1, 1, "", bgcolor=color.new(colorSuperFast, 40))
    
    table.cell(tblColorLegend, 0, 2, "9 EMA", text_color=color.white)
    table.cell(tblColorLegend, 1, 2, "", bgcolor=color.new(colorFast, 40))
    
    table.cell(tblColorLegend, 0, 3, "50 EMA", text_color=color.white)
    table.cell(tblColorLegend, 1, 3, "", bgcolor=color.new(colorNormal, 40))
    
    table.cell(tblColorLegend, 0, 4, "200 EMA", text_color=color.white)
    table.cell(tblColorLegend, 1, 4, "", bgcolor=color.new(colorLazy, 40))
    
    table.cell(tblColorLegend, 0, 5, "1H 50 EMA", text_color=color.white)
    table.cell(tblColorLegend, 1, 5, "", bgcolor=color.new(colorNormal1H, 60))
    
    table.cell(tblColorLegend, 0, 6, "1H 200 EMA", text_color=color.white)
    table.cell(tblColorLegend, 1, 6, "", bgcolor=color.new(colorLazy1H, 60))
    
    table.cell(tblColorLegend, 0, 7, "4H 50 EMA", text_color=color.white)
    table.cell(tblColorLegend, 1, 7, "", bgcolor=color.new(colorNormal4H, 80))
    
    table.cell(tblColorLegend, 0, 8, "4H 200 EMA", text_color=color.white)
    table.cell(tblColorLegend, 1, 8, "", bgcolor=color.new(colorLazy4H, 80))
    
    // Bollinger Bands Legend
    table.cell(tblColorLegend, 0, 9, "Current BB", text_color=color.white)
    table.cell(tblColorLegend, 1, 9, "", bgcolor=color.new(colorBB, 30))
    
    table.cell(tblColorLegend, 0, 10, "1H BB", text_color=color.white)
    table.cell(tblColorLegend, 1, 10, "", bgcolor=color.new(colorBB1H, 50))
    
    table.cell(tblColorLegend, 0, 11, "4H BB", text_color=color.white)
    table.cell(tblColorLegend, 1, 11, "", bgcolor=color.new(colorBB4H, 70))

    table.cell(tblColorLegend, 0, 12, "1D BB", text_color=color.white)
    table.cell(tblColorLegend, 1, 12, "", bgcolor=color.new(colorBB1D, 70))
