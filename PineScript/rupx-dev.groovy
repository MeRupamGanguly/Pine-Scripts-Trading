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
showCurrentBB = input.bool(true, "Show Current TF BB", group="Bollinger Bands")
show1HBB = input.bool(true, "Show 1H BB", group="Bollinger Bands")
show4HBB = input.bool(true, "Show 4H BB", group="Bollinger Bands")
show1DBB = input.bool(true, "Show 1D BB", group="Bollinger Bands")

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

// ———— Plot Current TF Bollinger Bands ————
plot(showCurrentBB ? upperBB : na, "Upper BB", color.new(colorBB, 30), 1)
plot(showCurrentBB ? basis : na, "Mid BB", color.new(colorBB, 30), 1)
plot(showCurrentBB ? lowerBB : na, "Lower BB", color.new(colorBB, 30), 1)

// ———— Plot 1H Bollinger Bands ————
plot(show1HBB ? h1Upper : na, "1H Upper", color.new(colorBB1H, 50), 1)
plot(show1HBB ? h1Basis : na, "1H Mid", color.new(colorBB1H, 50), 1)
plot(show1HBB ? h1Lower : na, "1H Lower", color.new(colorBB1H, 50), 1)

// ———— Plot 4H Bollinger Bands ————
plot(show4HBB ? h4Upper : na, "4H Upper", color.new(colorBB4H, 70), 1)
plot(show4HBB ? h4Basis : na, "4H Mid", color.new(colorBB4H, 70), 1)
plot(show4HBB ? h4Lower : na, "4H Lower", color.new(colorBB4H, 70), 1)

// ———— Plot 1D Bollinger Bands ————
plot(show1DBB ? d1Upper : na, "1D Upper", color.new(colorBB1D, 70), 1)
plot(show1DBB ? d1Basis : na, "1D Mid", color.new(colorBB1D, 70), 1)
plot(show1DBB ? d1Lower : na, "1D Lower", color.new(colorBB1D, 70), 1)

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



// ------------------------------------------ Table ---------------------------------------------------

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


// --------------------------------------- KILL Zone --------------------------------------------------

get_line_type(_style) =>
    result = switch _style
        'Solid' => line.style_solid
        'Dotted' => line.style_dotted
        'Dashed' => line.style_dashed
    result

get_size(x) =>
    result = switch x
        'Auto' => size.auto
        'Tiny' => size.tiny
        'Small' => size.small
        'Normal' => size.normal
        'Large' => size.large
        'Huge' => size.huge
        
get_table_pos(pos) =>
    result = switch pos
        "Bottom Center" => position.bottom_center
        "Bottom Left" => position.bottom_left
        "Bottom Right" => position.bottom_right
        "Middle Center" => position.middle_center
        "Middle Left" => position.middle_left
        "Middle Right" => position.middle_right
        "Top Center" => position.top_center
        "Top Left" => position.top_left
        "Top Right" => position.top_right

var g_SETTINGS      = "Settings"
max_days            = input.int(15, "Session Drawing Limit", 1, tooltip = "Only this many drawings will be kept on the chart, for each selected drawing type (killzone boxes, pivot lines, open lines, etc.)", group = g_SETTINGS)
tf_limit            = input.timeframe("30", "Timeframe Limit", tooltip = "Drawings will not appear on timeframes greater than or equal to this", group = g_SETTINGS)
gmt_tz              = input.string('America/New_York', "Timezone", options = ['America/New_York','GMT-12','GMT-11','GMT-10','GMT-9','GMT-8','GMT-7','GMT-6','GMT-5','GMT-4','GMT-3','GMT-2','GMT-1','GMT+0','GMT+1','GMT+2','GMT+3','GMT+4','GMT+5','GMT+6','GMT+7','GMT+8','GMT+9','GMT+10','GMT+11','GMT+12','GMT+13','GMT+14'], tooltip = "Note GMT is not adjusted to reflect Daylight Saving Time changes", group = g_SETTINGS)
lbl_size            = get_size(input.string('Normal', "Label Size", options = ['Auto', 'Tiny', 'Small', 'Normal', 'Large', 'Huge'], tooltip = "The size of all labels", group = g_SETTINGS))
txt_color           = input.color(color.black, "Text Color", tooltip = "The color of all label and table text", group = g_SETTINGS)
use_cutoff          = input.bool(false, "Drawing Cutoff Time", inline = "CO", tooltip = "When enabled, all pivots and open price lines will stop extending at this time", group = g_SETTINGS)
cutoff              = input.session("1800-1801", "", inline = "CO", group = g_SETTINGS)


var g_KZ            = "Killzones"
show_kz             = input.bool(true, "Show Killzone Boxes", inline = "KZ", group = g_KZ)
show_kz_text        = input.bool(true, "Display Text", inline = "KZ", group = g_KZ)

use_asia            = input.bool(true, "", inline = "ASIA", group = g_KZ)
as_txt              = input.string("Asia", "", inline = "ASIA", group = g_KZ)
asia                = input.session("2000-0000", "", inline = "ASIA", group = g_KZ)
as_color            = input.color(color.blue, "", inline = "ASIA", group = g_KZ)

use_london          = input.bool(true, "", inline = "LONDON", group = g_KZ)
lo_txt              = input.string("London", "", inline = "LONDON", group = g_KZ)
london              = input.session("0200-0500", "", inline = "LONDON", group = g_KZ)
lo_color            = input.color(color.red, "", inline = "LONDON", group = g_KZ)

use_nyam            = input.bool(true, "", inline = "NYAM", group = g_KZ)
na_txt              = input.string("NY AM", "", inline = "NYAM", group = g_KZ)
nyam                = input.session("0930-1100", "", inline = "NYAM", group = g_KZ)
na_color            = input.color(#089981, "", inline = "NYAM", group = g_KZ)

use_nylu            = input.bool(true, "", inline = "NYLU", group = g_KZ)
nl_txt              = input.string("NY Lunch", "", inline = "NYLU", group = g_KZ)
nylu                = input.session("1200-1300", "", inline = "NYLU", group = g_KZ)
nl_color            = input.color(color.yellow, "", inline = "NYLU", group = g_KZ)

use_nypm            = input.bool(true, "", inline = "NYPM", group = g_KZ)
np_txt              = input.string("NY PM", "", inline = "NYPM", group = g_KZ)
nypm                = input.session("1330-1600", "", inline = "NYPM", group = g_KZ)
np_color            = input.color(color.purple, "", inline = "NYPM", group = g_KZ)

box_transparency    = input.int(70, "Box Transparency", 0, 100, group = g_KZ)
text_transparency   = input.int(50, "Text Transparency", 0, 100, group = g_KZ)


var g_LABELS        = "Killzone Pivots"
show_pivots         = input.bool(true, "Show Pivots", inline = "PV", group = g_LABELS)
use_alerts          = input.bool(true, "Alert Broken Pivots", inline = "PV", tooltip = "The desired killzones must be enabled at the time that an alert is created, along with the show pivots option, in order for alerts to work", group = g_LABELS)
show_midpoints      = input.bool(false, "Show Pivot Midpoints", inline = "mp", group = g_LABELS)
stop_midpoints      = input.bool(true, "Stop Once Mitigated", inline = "mp", group = g_LABELS) 
show_labels         = input.bool(true, "Show Pivot Labels", inline = "LB", tooltip = "Show labels denoting each killzone's high and low. Optionally choose to show the price of each level. Right side will show labels on the right-hand side of the chart until they are reached", group = g_LABELS)
label_price         = input.bool(false, "Display Price", inline = "LB", group = g_LABELS)
label_right         = input.bool(false, "Right Side", inline = "LB", group = g_LABELS)
ext_pivots          = input.string("Until Mitigated", "Extend Pivots...", options = ['Until Mitigated', 'Past Mitigation'], group = g_LABELS)
ext_which           = input.string("Most Recent", "...From Which Sessions", options = ['Most Recent', 'All'], group = g_LABELS)

ash_str             = input.string("AS.H", "Killzone 1 Labels", inline = "L_AS", group = g_LABELS)
asl_str             = input.string("AS.L", "", inline = "L_AS", group = g_LABELS)

loh_str             = input.string("LO.H", "Killzone 2 Labels", inline = "L_LO", group = g_LABELS)
lol_str             = input.string("LO.L", "", inline = "L_LO", group = g_LABELS)

nah_str             = input.string("NYAM.H", "Killzone 3 Labels", inline = "L_NA", group = g_LABELS)
nal_str             = input.string("NYAM.L", "", inline = "L_NA", group = g_LABELS)

nlh_str             = input.string("NYL.H", "Killzone 4 Labels", inline = "L_NL", group = g_LABELS)
nll_str             = input.string("NYL.L", "", inline = "L_NL", group = g_LABELS)

nph_str             = input.string("NYPM.H", "Killzone 5 Labels", inline = "L_NP", group = g_LABELS)
npl_str             = input.string("NYPM.L", "", inline = "L_NP", group = g_LABELS)

kzp_style           = get_line_type(input.string(defval = 'Solid', title = "Pivot Style", options = ['Solid', 'Dotted', 'Dashed'], inline = "KZP", group = g_LABELS))
kzp_width           = input.int(1, "", inline = "KZP", group = g_LABELS)
kzm_style           = get_line_type(input.string(defval = 'Dotted', title = "Midpoint Style", options = ['Solid', 'Dotted', 'Dashed'], inline = "KZM", group = g_LABELS))
kzm_width           = input.int(1, "", inline = "KZM", group = g_LABELS)


var g_RNG           = "Killzone Range"
show_range          = input.bool(false, "Show Killzone Range", tooltip = "Show the most recent ranges of each selected killzone, from high to low", group = g_RNG)
show_range_avg      = input.bool(true, "Show Average", tooltip = "Show the average range of each selected killzone", group = g_RNG)
range_avg           = input.int(5, "Average Length", 0, tooltip = "This many previous sessions will be used to calculate the average. If there isn't enough data on the current chart, it will use as many sessions as possible", group = g_RNG)
range_pos           = get_table_pos(input.string('Top Right', "Table Position", options = ['Bottom Center', 'Bottom Left', 'Bottom Right', 'Middle Center', 'Middle Left', 'Middle Right', 'Top Center', 'Top Left', 'Top Right'], group = g_RNG))
range_size          = get_size(input.string('Normal', "Table Size", options = ['Auto', 'Tiny', 'Small', 'Normal', 'Large', 'Huge'], group = g_RNG))


var g_DWM           = "Day - Week - Month"
sep_unlimited       = input.bool(false, "Unlimited", tooltip = "Unlimited will show as many of the selected lines as possible. Otherwise, the session drawing limit will be used", group = g_DWM)
alert_HL            = input.bool(false, "Alert High/Low Break", tooltip = "Alert when any selected highs and lows are traded through. The desired timeframe's high/low option must be enabled at the time that an alert is created", group = g_DWM)

show_d_open         = input.bool(false, "D Open", inline = "DO", group = g_DWM)
dhl                 = input.bool(false, "High/Low", inline = "DO", tooltip = "", group = g_DWM)
ds                  = input.bool(false, "Separators", inline = "DO", tooltip = "Mark where a new day begins", group = g_DWM)
d_color             = input.color(color.blue, "", inline = "DO", group = g_DWM)

show_w_open         = input.bool(false, "W Open", inline = "WO", group = g_DWM)
whl                 = input.bool(false, "High/Low", inline = "WO", tooltip = "", group = g_DWM)
ws                  = input.bool(false, "Separators", inline = "WO", tooltip = "Mark where a new week begins", group = g_DWM)
w_color             = input.color(#089981, "", inline = "WO", group = g_DWM)

show_m_open         = input.bool(false, "M Open", inline = "MO", group = g_DWM)
mhl                 = input.bool(false, "High/Low", inline = "MO", tooltip = "", group = g_DWM)
ms                  = input.bool(false, "Separators", inline = "MO", tooltip = "Mark where a new month begins", group = g_DWM)
m_color             = input.color(color.red, "", inline = "MO", group = g_DWM)

htf_style           = get_line_type(input.string(defval = 'Solid', title = "Style", options = ['Solid', 'Dotted', 'Dashed'], inline = "D0", group = g_DWM))
htf_width           = input.int(1, "", inline = "D0", group = g_DWM)

dow_labels          = input.bool(true, "Day of Week Labels", inline = "DOW", group = g_DWM)
dow_yloc            = input.string('Bottom', "", options = ['Top', 'Bottom'], inline = "DOW", group = g_DWM)
dow_xloc            = input.string('Midnight', "", options = ['Midnight', 'Midday'], inline = "DOW", group = g_DWM)
dow_hide_wknd       = input.bool(true, "Hide Weekend Labels", group = g_DWM)


var g_OPEN          = "Opening Prices"
open_unlimited      = input.bool(false, "Unlimited", tooltip = "Unlimited will show as many of the selected lines as possible. Otherwise, the session drawing limit will be used", group = g_OPEN)

use_h1              = input.bool(false, "", inline = "H1", group = g_OPEN)
h1_text             = input.string("True Day Open", "", inline = "H1", group = g_OPEN)
h1                  = input.session("0000-0001", "", inline = "H1", group = g_OPEN)
h1_color            = input.color(color.black, "", inline = "H1", group = g_OPEN)

use_h2              = input.bool(false, "", inline = "H2", group = g_OPEN)
h2_text             = input.string("06:00", "", inline = "H2", group = g_OPEN)
h2                  = input.session("0600-0601", "", inline = "H2", group = g_OPEN)
h2_color            = input.color(color.black, "", inline = "H2", group = g_OPEN)

use_h3              = input.bool(false, "", inline = "H3", group = g_OPEN)
h3_text             = input.string("10:00", "", inline = "H3", group = g_OPEN)
h3                  = input.session("1000-1001", "", inline = "H3", group = g_OPEN)
h3_color            = input.color(color.black, "", inline = "H3", group = g_OPEN)

use_h4              = input.bool(false, "", inline = "H4", group = g_OPEN)
h4_text             = input.string("14:00", "", inline = "H4", group = g_OPEN)
h4                  = input.session("1400-1401", "", inline = "H4", group = g_OPEN)
h4_color            = input.color(color.black, "", inline = "H4", group = g_OPEN)

use_h5              = input.bool(false, "", inline = "H5", group = g_OPEN)
h5_text             = input.string("00:00", "", inline = "H5", group = g_OPEN)
h5                  = input.session("0000-0001", "", inline = "H5", group = g_OPEN)
h5_color            = input.color(color.black, "", inline = "H5", group = g_OPEN)

use_h6              = input.bool(false, "", inline = "H6", group = g_OPEN)
h6_text             = input.string("00:00", "", inline = "H6", group = g_OPEN)
h6                  = input.session("0000-0001", "", inline = "H6", group = g_OPEN)
h6_color            = input.color(color.black, "", inline = "H6", group = g_OPEN)

use_h7              = input.bool(false, "", inline = "H7", group = g_OPEN)
h7_text             = input.string("00:00", "", inline = "H7", group = g_OPEN)
h7                  = input.session("0000-0001", "", inline = "H7", group = g_OPEN)
h7_color            = input.color(color.black, "", inline = "H7", group = g_OPEN)

use_h8              = input.bool(false, "", inline = "H8", group = g_OPEN)
h8_text             = input.string("00:00", "", inline = "H8", group = g_OPEN)
h8                  = input.session("0000-0001", "", inline = "H8", group = g_OPEN)
h8_color            = input.color(color.black, "", inline = "H8", group = g_OPEN)

hz_style            = get_line_type(input.string(defval = 'Dotted', title = "Style", options = ['Solid', 'Dotted', 'Dashed'], inline = "H0", group = g_OPEN))
hz_width            = input.int(1, "", inline = "H0", group = g_OPEN)


var g_VERTICAL      = "Timestamps"
v_unlimited         = input.bool(false, "Unlimited", tooltip = "Unlimited will show as many of the selected lines as possible. Otherwise, the session drawing limit will be used", group = g_VERTICAL)

use_v1              = input.bool(false, "", inline = "V1", group = g_VERTICAL)
v1                  = input.session("0000-0001", "", inline = "V1", group = g_VERTICAL)
v1_color            = input.color(color.black, "", inline = "V1", group = g_VERTICAL)

use_v2              = input.bool(false, "", inline = "V2", group = g_VERTICAL)
v2                  = input.session("0800-0801", "", inline = "V2", group = g_VERTICAL)
v2_color            = input.color(color.black, "", inline = "V2", group = g_VERTICAL)

use_v3              = input.bool(false, "", inline = "V3", group = g_VERTICAL)
v3                  = input.session("1000-1001", "", inline = "V3", group = g_VERTICAL)
v3_color            = input.color(color.black, "", inline = "V3", group = g_VERTICAL)

use_v4              = input.bool(false, "", inline = "V4", group = g_VERTICAL)
v4                  = input.session("1200-1201", "", inline = "V4", group = g_VERTICAL)
v4_color            = input.color(color.black, "", inline = "V4", group = g_VERTICAL)

vl_style            = get_line_type(input.string(defval = 'Dotted', title = "Style", options = ['Solid', 'Dotted', 'Dashed'], inline = "V0", group = g_VERTICAL))
vl_width            = input.int(1, "", inline = "V0", group = g_VERTICAL)

type kz
    string _title

    box[] _box

    line[] _hi_line
    line[] _md_line
    line[] _lo_line

    label[] _hi_label
    label[] _lo_label

    bool[] _hi_valid
    bool[] _md_valid
    bool[] _lo_valid

    float[] _range_store
    float _range_current

type hz
    line[] LN
    label[] LB
    bool[] CO

type dwm_hl
    line[] hi_line
    line[] lo_line
    label[] hi_label
    label[] lo_label
    bool hit_high = false
    bool hit_low = false

type dwm_info
    string tf
    float o = na
    float h = na
    float l = na
    float ph = na
    float pl = na

var as_kz = kz.new(as_txt, array.new_box(), array.new_line(), array.new_line(), array.new_line(), array.new_label(), array.new_label(), array.new_bool(), array.new_bool(), array.new_bool(), array.new_float())
var lo_kz = kz.new(lo_txt, array.new_box(), array.new_line(), array.new_line(), array.new_line(), array.new_label(), array.new_label(), array.new_bool(), array.new_bool(), array.new_bool(), array.new_float())
var na_kz = kz.new(na_txt, array.new_box(), array.new_line(), array.new_line(), array.new_line(), array.new_label(), array.new_label(), array.new_bool(), array.new_bool(), array.new_bool(), array.new_float())
var nl_kz = kz.new(nl_txt, array.new_box(), array.new_line(), array.new_line(), array.new_line(), array.new_label(), array.new_label(), array.new_bool(), array.new_bool(), array.new_bool(), array.new_float())
var np_kz = kz.new(np_txt, array.new_box(), array.new_line(), array.new_line(), array.new_line(), array.new_label(), array.new_label(), array.new_bool(), array.new_bool(), array.new_bool(), array.new_float())

var hz_1 = hz.new(array.new_line(), array.new_label(), array.new_bool())
var hz_2 = hz.new(array.new_line(), array.new_label(), array.new_bool())
var hz_3 = hz.new(array.new_line(), array.new_label(), array.new_bool())
var hz_4 = hz.new(array.new_line(), array.new_label(), array.new_bool())
var hz_5 = hz.new(array.new_line(), array.new_label(), array.new_bool())
var hz_6 = hz.new(array.new_line(), array.new_label(), array.new_bool())
var hz_7 = hz.new(array.new_line(), array.new_label(), array.new_bool())
var hz_8 = hz.new(array.new_line(), array.new_label(), array.new_bool())

var d_hl = dwm_hl.new(array.new_line(), array.new_line(), array.new_label(), array.new_label())
var w_hl = dwm_hl.new(array.new_line(), array.new_line(), array.new_label(), array.new_label())
var m_hl = dwm_hl.new(array.new_line(), array.new_line(), array.new_label(), array.new_label())

var d_info = dwm_info.new("D")
var w_info = dwm_info.new("W")
var m_info = dwm_info.new("M")

t_as = not na(time("", asia, gmt_tz))
t_lo = not na(time("", london, gmt_tz))
t_na = not na(time("", nyam, gmt_tz))
t_nl = not na(time("", nylu, gmt_tz))
t_np = not na(time("", nypm, gmt_tz))
t_co = not na(time("", cutoff, gmt_tz))

t_h1 = not na(time("", h1, gmt_tz))
t_h2 = not na(time("", h2, gmt_tz))
t_h3 = not na(time("", h3, gmt_tz))
t_h4 = not na(time("", h4, gmt_tz))
t_h5 = not na(time("", h5, gmt_tz))
t_h6 = not na(time("", h6, gmt_tz))
t_h7 = not na(time("", h7, gmt_tz))
t_h8 = not na(time("", h8, gmt_tz))

t_v1 = not na(time("", v1, gmt_tz))
t_v2 = not na(time("", v2, gmt_tz))
t_v3 = not na(time("", v3, gmt_tz))
t_v4 = not na(time("", v4, gmt_tz))

var d_sep_line = array.new_line()
var w_sep_line = array.new_line()
var m_sep_line = array.new_line()

var d_line = array.new_line()
var w_line = array.new_line()
var m_line = array.new_line()

var d_label = array.new_label()
var w_label = array.new_label()
var m_label = array.new_label()

var v1_line = array.new_line()
var v2_line = array.new_line()
var v3_line = array.new_line()
var v4_line = array.new_line()

var transparent = #ffffff00
var ext_current = ext_which == 'Most Recent'
var ext_past = ext_pivots == 'Past Mitigation'

update_dwm_info(dwm_info n) =>
    if timeframe.change(n.tf)
        n.ph := n.h
        n.pl := n.l
        n.o := open
        n.h := high
        n.l := low
    else
        n.h := math.max(high, n.h)
        n.l := math.min(low,  n.l)

if dhl or show_d_open
    update_dwm_info(d_info)
if whl or show_w_open
    update_dwm_info(w_info)
if mhl or show_m_open
    update_dwm_info(m_info)

get_box_color(color c) =>
    result = color.new(c, box_transparency)
    
get_text_color(color c) =>
    result = color.new(c, text_transparency)

dwm_sep(string tf, bool use, line[] arr, color col) =>
    if use
        if timeframe.change(tf)
            arr.unshift(line.new(bar_index, high*1.0001, bar_index, low, style = htf_style, width = htf_width, extend = extend.both, color = col))
            if not sep_unlimited and arr.size() > max_days
                arr.pop().delete()
                

dwm_open(string tf, bool use, line[] lns, label[] lbls, dwm_info n, color col) =>
    if use
        if lns.size() > 0
            lns.get(0).set_x2(time)
            lbls.get(0).set_x(time)
        if timeframe.change(tf)
            lns.unshift(line.new(time, n.o, time, n.o, xloc = xloc.bar_time, style = htf_style, width = htf_width, color = col))
            lbls.unshift(label.new(time, n.o, tf + " OPEN", xloc = xloc.bar_time, style = label.style_label_left, color = transparent, textcolor = txt_color, size = lbl_size))
            if not sep_unlimited and lns.size() > max_days
                lns.pop().delete()
                lbls.pop().delete()


dwm_hl(string tf, bool use, dwm_hl hl, dwm_info n, color col) =>
    if use
        if hl.hi_line.size() > 0
            hl.hi_line.get(0).set_x2(time)
            hl.lo_line.get(0).set_x2(time)
            hl.hi_label.get(0).set_x(time)
            hl.lo_label.get(0).set_x(time)
        if timeframe.change(tf)
            hl.hi_line.unshift(line.new(time, n.ph, time, n.ph, xloc = xloc.bar_time, style = htf_style, width = htf_width, color = col))
            hl.lo_line.unshift(line.new(time, n.pl, time, n.pl, xloc = xloc.bar_time, style = htf_style, width = htf_width, color = col))
            hl.hi_label.unshift(label.new(time, n.ph, "P"+tf+"H", xloc = xloc.bar_time, style = label.style_label_left, color = transparent, textcolor = txt_color, size = lbl_size))
            hl.lo_label.unshift(label.new(time, n.pl, "P"+tf+"L", xloc = xloc.bar_time, style = label.style_label_left, color = transparent, textcolor = txt_color, size = lbl_size))
            hl.hit_high := false
            hl.hit_low := false
            if not sep_unlimited and hl.hi_line.size() > max_days
                hl.hi_line.pop().delete()
                hl.lo_line.pop().delete()
                hl.hi_label.pop().delete()
                hl.lo_label.pop().delete()
        if hl.hi_line.size() > 0 and alert_HL
            if not hl.hit_high and high > hl.hi_line.get(0).get_y1()
                hl.hit_high := true
                alert(str.format("Hit P{0}H", tf))
            if not hl.hit_low and low < hl.lo_line.get(0).get_y1()
                hl.hit_low := true
                alert(str.format("Hit P{0}L", tf))


dwm() =>
    if timeframe.in_seconds("") <= timeframe.in_seconds(tf_limit)
        // DWM - Separators
        dwm_sep("D", ds, d_sep_line, d_color)
        dwm_sep("W", ws, w_sep_line, w_color)
        dwm_sep("M", ms, m_sep_line, m_color)

        // DWM - Open Lines
        dwm_open("D", show_d_open, d_line, d_label, d_info, d_color)
        dwm_open("W", show_w_open, w_line, w_label, w_info, w_color)
        dwm_open("M", show_m_open, m_line, m_label, m_info, m_color)

        // DWM - Highs and Lows
        dwm_hl("D", dhl, d_hl, d_info, d_color)
        dwm_hl("W", whl, w_hl, w_info, w_color)
        dwm_hl("M", mhl, m_hl, m_info, m_color)


vline(bool use, bool t, line[] arr, color col) =>
    if use
        if t and not t[1]
            arr.unshift(line.new(bar_index, high*1.0001, bar_index, low, style = vl_style, width = vl_width, extend = extend.both, color = col))
        if not v_unlimited
            if arr.size() > max_days
                arr.pop().delete()

                
vlines() =>
    if timeframe.in_seconds("") <= timeframe.in_seconds(tf_limit)
        vline(use_v1, t_v1, v1_line, v1_color)
        vline(use_v2, t_v2, v2_line, v2_color)
        vline(use_v3, t_v3, v3_line, v3_color)
        vline(use_v4, t_v4, v4_line, v4_color)


hz_line(bool use, bool t, hz hz, string txt, color col) =>
    if use
        if t and not t[1]
            hz.LN.unshift(line.new(bar_index, open, bar_index, open, style = hz_style, width = hz_width, color = col))
            hz.LB.unshift(label.new(bar_index, open, txt, style = label.style_label_left, color = transparent, textcolor = txt_color, size = lbl_size))
            array.unshift(hz.CO, false)
            if not open_unlimited and hz.LN.size() > max_days
                hz.LN.pop().delete()
                hz.LB.pop().delete()
                hz.CO.pop()
        if not t and hz.CO.size() > 0
            if not hz.CO.get(0)
                hz.LN.get(0).set_x2(bar_index)
                hz.LB.get(0).set_x(bar_index)
                if (use_cutoff ? t_co : false)
                    hz.CO.set(0, true)


hz_lines() =>
    if timeframe.in_seconds("") <= timeframe.in_seconds(tf_limit)
        hz_line(use_h1, t_h1, hz_1, h1_text, h1_color)
        hz_line(use_h2, t_h2, hz_2, h2_text, h2_color)
        hz_line(use_h3, t_h3, hz_3, h3_text, h3_color)
        hz_line(use_h4, t_h4, hz_4, h4_text, h4_color)
        hz_line(use_h5, t_h5, hz_5, h5_text, h5_color)
        hz_line(use_h6, t_h6, hz_6, h6_text, h6_color)
        hz_line(use_h7, t_h7, hz_7, h7_text, h7_color)
        hz_line(use_h8, t_h8, hz_8, h8_text, h8_color)


del_kz(kz k) =>
    if k._box.size() > max_days
        k._box.pop().delete()
    if k._hi_line.size() > max_days
        k._hi_line.pop().delete()
        k._lo_line.pop().delete()
        k._hi_valid.pop()
        k._lo_valid.pop()
        if show_midpoints
            k._md_line.pop().delete()
            k._md_valid.pop()
    if k._hi_label.size() > max_days
        k._hi_label.pop().delete()
        k._lo_label.pop().delete()

update_price_string(label L, float P) =>
    S = L.get_text()
    pre = str.substring(S, 0, str.pos(S, " ")) 
    str.trim(pre)
    L.set_text(str.format("{0} ({1})", pre, P))

adjust_in_kz(kz kz, bool t) =>
    if t
        kz._box.get(0).set_right(time)
        kz._box.get(0).set_top(math.max(kz._box.get(0).get_top(), high))
        kz._box.get(0).set_bottom(math.min(kz._box.get(0).get_bottom(), low))

        kz._range_current := kz._box.get(0).get_top() - kz._box.get(0).get_bottom()

        if show_pivots and kz._hi_line.size() > 0
            kz._hi_line.get(0).set_x2(time)
            if high > kz._hi_line.get(0).get_y1()
                kz._hi_line.get(0).set_xy1(time, high)
                kz._hi_line.get(0).set_xy2(time, high)

            kz._lo_line.get(0).set_x2(time)
            if low < kz._lo_line.get(0).get_y1()
                kz._lo_line.get(0).set_xy1(time, low)
                kz._lo_line.get(0).set_xy2(time, low)
                
            if show_midpoints
                kz._md_line.get(0).set_x2(time)
                kz._md_line.get(0).set_xy1(time, math.avg(kz._hi_line.get(0).get_y2(), kz._lo_line.get(0).get_y2()))
                kz._md_line.get(0).set_xy2(time, math.avg(kz._hi_line.get(0).get_y2(), kz._lo_line.get(0).get_y2()))

        if show_labels and kz._hi_label.size() > 0
            if label_right
                kz._hi_label.get(0).set_x(time)
                kz._lo_label.get(0).set_x(time)
            if high > kz._hi_label.get(0).get_y()
                kz._hi_label.get(0).set_xy(time, high)
                if label_price
                    update_price_string(kz._hi_label.get(0), high)
            if low < kz._lo_label.get(0).get_y()
                kz._lo_label.get(0).set_xy(time, low)
                if label_price
                    update_price_string(kz._lo_label.get(0), low)
    

adjust_out_kz(kz kz, bool t) =>
    if not t and kz._box.size() > 0
        if t[1]
            array.unshift(kz._range_store, kz._range_current)
            if kz._range_store.size() > range_avg
                kz._range_store.pop()

    if kz._box.size() > 0 and show_pivots
        for i = 0 to kz._box.size() - 1
            if not ext_current or (ext_current and i == 0)
                if ext_past ? true : (kz._hi_valid.get(i) == true)
                    kz._hi_line.get(i).set_x2(time)
                    if show_labels and label_right
                        kz._hi_label.get(i).set_x(time)
                if high > kz._hi_line.get(i).get_y1() and kz._hi_valid.get(i) == true
                    if use_alerts and i == 0
                        alert("Broke "+kz._title+" High", alert.freq_once_per_bar)
                    kz._hi_valid.set(i, false)
                    if show_labels and label_right
                        kz._hi_label.get(0).set_style(label.style_label_down)
                else if (use_cutoff ? t_co : false)
                    kz._hi_valid.set(i, false)
                    
                if ext_past ? true : (kz._lo_valid.get(i) == true)
                    kz._lo_line.get(i).set_x2(time)
                    if show_labels and label_right
                        kz._lo_label.get(i).set_x(time)
                if low < kz._lo_line.get(i).get_y1() and kz._lo_valid.get(i) == true
                    if use_alerts and i == 0
                        alert("Broke "+kz._title+" Low", alert.freq_once_per_bar)
                    kz._lo_valid.set(i, false)
                    if show_labels and label_right
                        kz._lo_label.get(0).set_style(label.style_label_up)
                else if (use_cutoff ? t_co : false)
                    kz._lo_valid.set(i, false)
                    
                if show_midpoints and not t
                    if stop_midpoints ? (kz._md_valid.get(i) == true) : true
                        kz._md_line.get(i).set_x2(time)
                        if kz._md_valid.get(i) == true and low <= kz._md_line.get(i).get_y1() and high >= kz._md_line.get(i).get_y1()
                            kz._md_valid.set(i, false)

            else
                break


manage_kz(kz kz, bool use, bool t, color c, string box_txt, string hi_txt, string lo_txt) => 
    if timeframe.in_seconds("") <= timeframe.in_seconds(tf_limit) and use
        if t and not t[1]
            _c = get_box_color(c)
            _t = get_text_color(c)
            kz._box.unshift(box.new(time, high, time, low, xloc = xloc.bar_time, border_color = show_kz ? _c : na, bgcolor = show_kz ? _c : na, text = (show_kz and show_kz_text) ? box_txt : na, text_color = _t))

            if show_pivots
                kz._hi_line.unshift(line.new(time, high, time, high, xloc = xloc.bar_time, style = kzp_style, color = c, width = kzp_width))
                kz._lo_line.unshift(line.new(time, low, time, low, xloc = xloc.bar_time, style = kzp_style, color = c, width = kzp_width))
                if show_midpoints
                    kz._md_line.unshift(line.new(time, math.avg(high, low), time, math.avg(high, low), xloc = xloc.bar_time, style = kzm_style, color = c, width = kzm_width))
                    array.unshift(kz._md_valid, true)
                
                array.unshift(kz._hi_valid, true)
                array.unshift(kz._lo_valid, true) 

                if show_labels
                    _hi_txt = label_price ? str.format("{0} ({1})", hi_txt, high) : hi_txt
                    _lo_txt = label_price ? str.format("{0} ({1})", lo_txt, low)  : lo_txt
                    if label_right
                        kz._hi_label.unshift(label.new(time, high, _hi_txt, xloc = xloc.bar_time, color = transparent, textcolor = txt_color, style = label.style_label_left, size = lbl_size))
                        kz._lo_label.unshift(label.new(time, low,  _lo_txt, xloc = xloc.bar_time, color = transparent, textcolor = txt_color, style = label.style_label_left, size = lbl_size))
                    else
                        kz._hi_label.unshift(label.new(time, high, _hi_txt, xloc = xloc.bar_time, color = transparent, textcolor = txt_color, style = label.style_label_down, size = lbl_size))
                        kz._lo_label.unshift(label.new(time, low,  _lo_txt, xloc = xloc.bar_time, color = transparent, textcolor = txt_color, style = label.style_label_up, size = lbl_size))

            del_kz(kz)
        adjust_in_kz(kz, t)
        adjust_out_kz(kz, t)


manage_kz(as_kz, use_asia, t_as, as_color, as_txt, ash_str, asl_str)
manage_kz(lo_kz, use_london, t_lo, lo_color, lo_txt, loh_str, lol_str)
manage_kz(na_kz, use_nyam, t_na, na_color, na_txt, nah_str, nal_str)
manage_kz(nl_kz, use_nylu, t_nl, nl_color, nl_txt, nlh_str, nll_str)
manage_kz(np_kz, use_nypm, t_np, np_color, np_txt, nph_str, npl_str)

dwm() 
vlines()
hz_lines()

new_dow_time = dow_xloc == 'Midday' ? time - timeframe.in_seconds("D") / 2 * 1000 : time
new_day = dayofweek(new_dow_time, gmt_tz) != dayofweek(new_dow_time, gmt_tz)[1]

var dow_top = dow_yloc == 'Top'

var saturday = "SATURDAY"
var sunday = "SUNDAY"
var monday = "MONDAY"
var tuesday = "TUESDAY"
var wednesday = "WEDNESDAY"
var thursday = "THURSDAY"
var friday = "FRIDAY"

plotchar(dow_labels and timeframe.isintraday and dayofweek(new_dow_time, gmt_tz) == 1 and new_day and not dow_hide_wknd, location = dow_top ? location.top : location.bottom, char = "", textcolor = txt_color, text = sunday)
plotchar(dow_labels and timeframe.isintraday and dayofweek(new_dow_time, gmt_tz) == 2 and new_day, location = dow_top ? location.top : location.bottom, char = "", textcolor = txt_color, text = monday)
plotchar(dow_labels and timeframe.isintraday and dayofweek(new_dow_time, gmt_tz) == 3 and new_day, location = dow_top ? location.top : location.bottom, char = "", textcolor = txt_color, text = tuesday)
plotchar(dow_labels and timeframe.isintraday and dayofweek(new_dow_time, gmt_tz) == 4 and new_day, location = dow_top ? location.top : location.bottom, char = "", textcolor = txt_color, text = wednesday)
plotchar(dow_labels and timeframe.isintraday and dayofweek(new_dow_time, gmt_tz) == 5 and new_day, location = dow_top ? location.top : location.bottom, char = "", textcolor = txt_color, text = thursday)
plotchar(dow_labels and timeframe.isintraday and dayofweek(new_dow_time, gmt_tz) == 6 and new_day, location = dow_top ? location.top : location.bottom, char = "", textcolor = txt_color, text = friday)
plotchar(dow_labels and timeframe.isintraday and dayofweek(new_dow_time, gmt_tz) == 7 and new_day and not dow_hide_wknd, location = dow_top ? location.top : location.bottom, char = "", textcolor = txt_color, text = saturday)

get_min_days_stored() =>
    store = array.new_int()
    if as_kz._range_store.size() > 0 
        store.push(as_kz._range_store.size())
    if lo_kz._range_store.size() > 0 
        store.push(lo_kz._range_store.size())
    if na_kz._range_store.size() > 0 
        store.push(na_kz._range_store.size())
    if nl_kz._range_store.size() > 0 
        store.push(nl_kz._range_store.size())
    if np_kz._range_store.size() > 0 
        store.push(np_kz._range_store.size())
    result = store.min()

set_table(table tbl, kz kz, int row, string txt, bool use, bool t, color col) =>
    if use
        table.cell(tbl, 0, row, txt, text_size = range_size, bgcolor = get_box_color(col), text_color = txt_color)
        table.cell(tbl, 1, row, str.tostring(kz._range_current), text_size = range_size, bgcolor = t ? get_box_color(col) : na, text_color = txt_color)
        if show_range_avg
            table.cell(tbl, 2, row, str.tostring(kz._range_store.avg()), text_size = range_size, text_color = txt_color)

if show_range and barstate.islast
    var tbl = table.new(range_pos, 10, 10, chart.bg_color, chart.fg_color, 2, chart.fg_color, 1)

    table.cell(tbl, 0, 0, "Killzone", text_size = range_size, text_color = txt_color)
    table.cell(tbl, 1, 0, "Range", text_size = range_size, text_color = txt_color)
    if show_range_avg
        table.cell(tbl, 2, 0, "Avg ("+str.tostring(get_min_days_stored())+")", text_size = range_size, text_color = txt_color)

    set_table(tbl, as_kz, 1, as_txt, use_asia, t_as, as_color)
    set_table(tbl, lo_kz, 2, lo_txt, use_london, t_lo, lo_color)
    set_table(tbl, na_kz, 3, na_txt, use_nyam, t_na, na_color)
    set_table(tbl, nl_kz, 4, nl_txt, use_nylu, t_nl, nl_color)
    set_table(tbl, np_kz, 5, np_txt, use_nypm, t_np, np_color)



//------------------------------------------------- SMC --------------------------------------------------------------------

BULLISH_LEG                     = 1
BEARISH_LEG                     = 0

BULLISH                         = +1
BEARISH                         = -1

GREEN                           = #089981
RED                             = #F23645
BLUE                            = #2157f3
GRAY                            = #878b94
MONO_BULLISH                    = #b2b5be
MONO_BEARISH                    = #5d606b

HISTORICAL                      = 'Historical'
PRESENT                         = 'Present'

COLORED                         = 'Colored'
MONOCHROME                      = 'Monochrome'

ALL                             = 'All'
BOS                             = 'BOS'
CHOCH                           = 'CHoCH'

TINY                            = size.tiny
SMALL                           = size.small
NORMAL                          = size.normal

ATR                             = 'Atr'
RANGE                           = 'Cumulative Mean Range'

CLOSE                           = 'Close'
HIGHLOW                         = 'High/Low'

SOLID                           = '⎯⎯⎯'
DASHED                          = '----'
DOTTED                          = '····'

SMART_GROUP                     = 'Smart Money Concepts'
INTERNAL_GROUP                  = 'Real Time Internal Structure'
SWING_GROUP                     = 'Real Time Swing Structure'
BLOCKS_GROUP                    = 'Order Blocks'
EQUAL_GROUP                     = 'EQH/EQL'
GAPS_GROUP                      = 'Fair Value Gaps'
LEVELS_GROUP                    = 'Highs & Lows MTF'
ZONES_GROUP                     = 'Premium & Discount Zones'

modeTooltip                     = 'Allows to display historical Structure or only the recent ones'
styleTooltip                    = 'Indicator color theme'
showTrendTooltip                = 'Display additional candles with a color reflecting the current trend detected by structure'
showInternalsTooltip            = 'Display internal market structure'
internalFilterConfluenceTooltip = 'Filter non significant internal structure breakouts'
showStructureTooltip            = 'Display swing market Structure'
showSwingsTooltip               = 'Display swing point as labels on the chart'
showHighLowSwingsTooltip        = 'Highlight most recent strong and weak high/low points on the chart'
showInternalOrderBlocksTooltip  = 'Display internal order blocks on the chart\n\nNumber of internal order blocks to display on the chart'
showSwingOrderBlocksTooltip     = 'Display swing order blocks on the chart\n\nNumber of internal swing blocks to display on the chart'
orderBlockFilterTooltip         = 'Method used to filter out volatile order blocks \n\nIt is recommended to use the cumulative mean range method when a low amount of data is available'
orderBlockMitigationTooltip     = 'Select what values to use for order block mitigation'
showEqualHighsLowsTooltip       = 'Display equal highs and equal lows on the chart'
equalHighsLowsLengthTooltip     = 'Number of bars used to confirm equal highs and equal lows'
equalHighsLowsThresholdTooltip  = 'Sensitivity threshold in a range (0, 1) used for the detection of equal highs & lows\n\nLower values will return fewer but more pertinent results'
showFairValueGapsTooltip        = 'Display fair values gaps on the chart'
fairValueGapsThresholdTooltip   = 'Filter out non significant fair value gaps'
fairValueGapsTimeframeTooltip   = 'Fair value gaps timeframe'
fairValueGapsExtendTooltip      = 'Determine how many bars to extend the Fair Value Gap boxes on chart'
showPremiumDiscountZonesTooltip = 'Display premium, discount, and equilibrium zones on chart'

modeInput                       = input.string( HISTORICAL, 'Mode',                     group = SMART_GROUP,    tooltip = modeTooltip, options = [HISTORICAL, PRESENT])
styleInput                      = input.string( COLORED,    'Style',                    group = SMART_GROUP,    tooltip = styleTooltip,options = [COLORED, MONOCHROME])
showTrendInput                  = input(        false,      'Color Candles',            group = SMART_GROUP,    tooltip = showTrendTooltip)

showInternalsInput              = input(        true,       'Show Internal Structure',  group = INTERNAL_GROUP, tooltip = showInternalsTooltip)
showInternalBullInput           = input.string( ALL,        'Bullish Structure',        group = INTERNAL_GROUP, inline = 'ibull', options = [ALL,BOS,CHOCH])
internalBullColorInput          = input(        GREEN,      '',                         group = INTERNAL_GROUP, inline = 'ibull')
showInternalBearInput           = input.string( ALL,        'Bearish Structure' ,       group = INTERNAL_GROUP, inline = 'ibear', options = [ALL,BOS,CHOCH])
internalBearColorInput          = input(        RED,        '',                         group = INTERNAL_GROUP, inline = 'ibear')
internalFilterConfluenceInput   = input(        false,      'Confluence Filter',        group = INTERNAL_GROUP, tooltip = internalFilterConfluenceTooltip)
internalStructureSize           = input.string( TINY,       'Internal Label Size',      group = INTERNAL_GROUP, options = [TINY,SMALL,NORMAL])

showStructureInput              = input(        true,       'Show Swing Structure',     group = SWING_GROUP,    tooltip = showStructureTooltip)
showSwingBullInput              = input.string( ALL,        'Bullish Structure',        group = SWING_GROUP,    inline = 'bull',    options = [ALL,BOS,CHOCH])
swingBullColorInput             = input(        GREEN,      '',                         group = SWING_GROUP,    inline = 'bull')
showSwingBearInput              = input.string( ALL,        'Bearish Structure',        group = SWING_GROUP,    inline = 'bear',    options = [ALL,BOS,CHOCH])
swingBearColorInput             = input(        RED,        '',                         group = SWING_GROUP,    inline = 'bear')
swingStructureSize              = input.string( SMALL,      'Swing Label Size',         group = SWING_GROUP,    options = [TINY,SMALL,NORMAL])
showSwingsInput                 = input(        false,      'Show Swings Points',       group = SWING_GROUP,    tooltip = showSwingsTooltip,inline = 'swings')
swingsLengthInput               = input.int(    50,         '',                         group = SWING_GROUP,    minval = 10,                inline = 'swings')
showHighLowSwingsInput          = input(        true,       'Show Strong/Weak High/Low',group = SWING_GROUP,    tooltip = showHighLowSwingsTooltip)

showInternalOrderBlocksInput    = input(        true,       'Internal Order Blocks' ,   group = BLOCKS_GROUP,   tooltip = showInternalOrderBlocksTooltip,   inline = 'iob')
internalOrderBlocksSizeInput    = input.int(    5,          '',                         group = BLOCKS_GROUP,   minval = 1, maxval = 20,                    inline = 'iob')
showSwingOrderBlocksInput       = input(        false,      'Swing Order Blocks',       group = BLOCKS_GROUP,   tooltip = showSwingOrderBlocksTooltip,      inline = 'ob')
swingOrderBlocksSizeInput       = input.int(    5,          '',                         group = BLOCKS_GROUP,   minval = 1, maxval = 20,                    inline = 'ob') 
orderBlockFilterInput           = input.string( 'Atr',      'Order Block Filter',       group = BLOCKS_GROUP,   tooltip = orderBlockFilterTooltip,          options = [ATR, RANGE])
orderBlockMitigationInput       = input.string( HIGHLOW,    'Order Block Mitigation',   group = BLOCKS_GROUP,   tooltip = orderBlockMitigationTooltip,      options = [CLOSE,HIGHLOW])
internalBullishOrderBlockColor  = input.color(color.new(#3179f5, 80), 'Internal Bullish OB',    group = BLOCKS_GROUP)
internalBearishOrderBlockColor  = input.color(color.new(#f77c80, 80), 'Internal Bearish OB',    group = BLOCKS_GROUP)
swingBullishOrderBlockColor     = input.color(color.new(#1848cc, 80), 'Bullish OB',             group = BLOCKS_GROUP)
swingBearishOrderBlockColor     = input.color(color.new(#b22833, 80), 'Bearish OB',             group = BLOCKS_GROUP)

showEqualHighsLowsInput         = input(        true,       'Equal High/Low',           group = EQUAL_GROUP,    tooltip = showEqualHighsLowsTooltip)
equalHighsLowsLengthInput       = input.int(    3,          'Bars Confirmation',        group = EQUAL_GROUP,    tooltip = equalHighsLowsLengthTooltip,      minval = 1)
equalHighsLowsThresholdInput    = input.float(  0.1,        'Threshold',                group = EQUAL_GROUP,    tooltip = equalHighsLowsThresholdTooltip,   minval = 0, maxval = 0.5, step = 0.1)
equalHighsLowsSizeInput         = input.string( TINY,       'Label Size',               group = EQUAL_GROUP,    options = [TINY,SMALL,NORMAL])

showFairValueGapsInput          = input(        false,      'Fair Value Gaps',          group = GAPS_GROUP,     tooltip = showFairValueGapsTooltip)
fairValueGapsThresholdInput     = input(        true,       'Auto Threshold',           group = GAPS_GROUP,     tooltip = fairValueGapsThresholdTooltip)
fairValueGapsTimeframeInput     = input.timeframe('',       'Timeframe',                group = GAPS_GROUP,     tooltip = fairValueGapsTimeframeTooltip)
fairValueGapsBullColorInput     = input.color(color.new(#00ff68, 70), 'Bullish FVG' , group = GAPS_GROUP)
fairValueGapsBearColorInput     = input.color(color.new(#ff0008, 70), 'Bearish FVG' , group = GAPS_GROUP)
fairValueGapsExtendInput        = input.int(    1,          'Extend FVG',               group = GAPS_GROUP,     tooltip = fairValueGapsExtendTooltip,       minval = 0)

showDailyLevelsInput            = input(        false,      'Daily',    group = LEVELS_GROUP,   inline = 'daily')
dailyLevelsStyleInput           = input.string( SOLID,      '',         group = LEVELS_GROUP,   inline = 'daily',   options = [SOLID,DASHED,DOTTED])
dailyLevelsColorInput           = input(        BLUE,       '',         group = LEVELS_GROUP,   inline = 'daily')
showWeeklyLevelsInput           = input(        false,      'Weekly',   group = LEVELS_GROUP,   inline = 'weekly')
weeklyLevelsStyleInput          = input.string( SOLID,      '',         group = LEVELS_GROUP,   inline = 'weekly',  options = [SOLID,DASHED,DOTTED])
weeklyLevelsColorInput          = input(        BLUE,       '',         group = LEVELS_GROUP,   inline = 'weekly')
showMonthlyLevelsInput          = input(        false,      'Monthly',   group = LEVELS_GROUP,   inline = 'monthly')
monthlyLevelsStyleInput         = input.string( SOLID,      '',         group = LEVELS_GROUP,   inline = 'monthly', options = [SOLID,DASHED,DOTTED])
monthlyLevelsColorInput         = input(        BLUE,       '',         group = LEVELS_GROUP,   inline = 'monthly')

showPremiumDiscountZonesInput   = input(        false,      'Premium/Discount Zones',   group = ZONES_GROUP , tooltip = showPremiumDiscountZonesTooltip)
premiumZoneColorInput           = input.color(  RED,        'Premium Zone',             group = ZONES_GROUP)
equilibriumZoneColorInput       = input.color(  GRAY,       'Equilibrium Zone',         group = ZONES_GROUP)
discountZoneColorInput          = input.color(  GREEN,      'Discount Zone',            group = ZONES_GROUP)


// @type                            UDT representing alerts as bool fields
// @field internalBullishBOS        internal structure custom alert
// @field internalBearishBOS        internal structure custom alert
// @field internalBullishCHoCH      internal structure custom alert
// @field internalBearishCHoCH      internal structure custom alert
// @field swingBullishBOS           swing structure custom alert
// @field swingBearishBOS           swing structure custom alert
// @field swingBullishCHoCH         swing structure custom alert
// @field swingBearishCHoCH         swing structure custom alert
// @field internalBullishOrderBlock internal order block custom alert
// @field internalBearishOrderBlock internal order block custom alert
// @field swingBullishOrderBlock    swing order block custom alert
// @field swingBearishOrderBlock    swing order block custom alert
// @field equalHighs                equal high low custom alert
// @field equalLows                 equal high low custom alert
// @field bullishFairValueGap       fair value gap custom alert
// @field bearishFairValueGap       fair value gap custom alert
type alerts
    bool internalBullishBOS         = false
    bool internalBearishBOS         = false
    bool internalBullishCHoCH       = false
    bool internalBearishCHoCH       = false
    bool swingBullishBOS            = false
    bool swingBearishBOS            = false
    bool swingBullishCHoCH          = false
    bool swingBearishCHoCH          = false
    bool internalBullishOrderBlock  = false
    bool internalBearishOrderBlock  = false
    bool swingBullishOrderBlock     = false
    bool swingBearishOrderBlock     = false
    bool equalHighs                 = false
    bool equalLows                  = false
    bool bullishFairValueGap        = false
    bool bearishFairValueGap        = false

// @type                            UDT representing last swing extremes (top & bottom)
// @field top                       last top swing price
// @field bottom                    last bottom swing price
// @field barTime                   last swing bar time
// @field barIndex                  last swing bar index
// @field lastTopTime               last top swing time
// @field lastBottomTime            last bottom swing time
type trailingExtremes
    float top
    float bottom
    int barTime
    int barIndex
    int lastTopTime
    int lastBottomTime

// @type                            UDT representing Fair Value Gaps
// @field top                       top price
// @field bottom                    bottom price
// @field bias                      bias (BULLISH or BEARISH)
// @field topBox                    top box
// @field bottomBox                 bottom box
type fairValueGap
    float top
    float bottom
    int bias
    box topBox
    box bottomBox

// @type                            UDT representing trend bias
// @field bias                      BULLISH or BEARISH
type trend
    int bias    

// @type                            UDT representing Equal Highs Lows display
// @field l_ine                     displayed line
// @field l_abel                    displayed label
type equalDisplay
    line l_ine      = na
    label l_abel    = na

// @type                            UDT representing a pivot point (swing point) 
// @field currentLevel              current price level
// @field lastLevel                 last price level
// @field crossed                   true if price level is crossed
// @field barTime                   bar time
// @field barIndex                  bar index    
type pivot
    float currentLevel
    float lastLevel
    bool crossed
    int barTime     = time
    int barIndex    = bar_index

// @type                            UDT representing an order block
// @field barHigh                   bar high
// @field barLow                    bar low
// @field barTime                   bar time
// @field bias                      BULLISH or BEARISH
type orderBlock
    float barHigh
    float barLow
    int barTime    
    int bias

// @variable                        current swing pivot high    
var pivot swingHigh                 = pivot.new(na,na,false)
// @variable                        current swing pivot low
var pivot swingLow                  = pivot.new(na,na,false)
// @variable                        current internal pivot high
var pivot internalHigh              = pivot.new(na,na,false)
// @variable                        current internal pivot low
var pivot internalLow               = pivot.new(na,na,false)
// @variable                        current equal high pivot
var pivot equalHigh                 = pivot.new(na,na,false)
// @variable                        current equal low pivot
var pivot equalLow                  = pivot.new(na,na,false)
// @variable                        swing trend bias
var trend swingTrend                = trend.new(0)
// @variable                        internal trend bias
var trend internalTrend             = trend.new(0)
// @variable                        equal high display
var equalDisplay equalHighDisplay   = equalDisplay.new()
// @variable                        equal low display
var equalDisplay equalLowDisplay    = equalDisplay.new()
// @variable                        storage for fairValueGap UDTs
var array<fairValueGap> fairValueGaps = array.new<fairValueGap>()
// @variable                        storage for parsed highs
var array<float> parsedHighs        = array.new<float>()
// @variable                        storage for parsed lows
var array<float> parsedLows         = array.new<float>()
// @variable                        storage for raw highs
var array<float> highs              = array.new<float>()
// @variable                        storage for raw lows
var array<float> lows               = array.new<float>()
// @variable                        storage for bar time values
var array<int> times                = array.new<int>()
// @variable                        last trailing swing high and low
var trailingExtremes trailing       = trailingExtremes.new()
// @variable                                storage for orderBlock UDTs (swing order blocks)
var array<orderBlock> swingOrderBlocks      = array.new<orderBlock>()
// @variable                                storage for orderBlock UDTs (internal order blocks)
var array<orderBlock> internalOrderBlocks   = array.new<orderBlock>()
// @variable                                storage for swing order blocks boxes
var array<box> swingOrderBlocksBoxes        = array.new<box>()
// @variable                                storage for internal order blocks boxes
var array<box> internalOrderBlocksBoxes     = array.new<box>()
// @variable                        color for swing bullish structures
var swingBullishColor               = styleInput == MONOCHROME ? MONO_BULLISH : swingBullColorInput
// @variable                        color for swing bearish structures
var swingBearishColor               = styleInput == MONOCHROME ? MONO_BEARISH : swingBearColorInput
// @variable                        color for bullish fair value gaps
var fairValueGapBullishColor        = styleInput == MONOCHROME ? color.new(MONO_BULLISH,70) : fairValueGapsBullColorInput
// @variable                        color for bearish fair value gaps
var fairValueGapBearishColor        = styleInput == MONOCHROME ? color.new(MONO_BEARISH,70) : fairValueGapsBearColorInput
// @variable                        color for premium zone
var premiumZoneColor                = styleInput == MONOCHROME ? MONO_BEARISH : premiumZoneColorInput
// @variable                        color for discount zone
var discountZoneColor               = styleInput == MONOCHROME ? MONO_BULLISH : discountZoneColorInput 
// @variable                        bar index on current script iteration
varip int currentBarIndex           = bar_index
// @variable                        bar index on last script iteration
varip int lastBarIndex              = bar_index
// @variable                        alerts in current bar
alerts currentAlerts                = alerts.new()
// @variable                        time at start of chart
var initialTime                     = time

// we create the needed boxes for displaying order blocks at the first execution
if barstate.isfirst
    if showSwingOrderBlocksInput
        for index = 1 to swingOrderBlocksSizeInput
            swingOrderBlocksBoxes.push(box.new(na,na,na,na,xloc = xloc.bar_time,extend = extend.right))
    if showInternalOrderBlocksInput
        for index = 1 to internalOrderBlocksSizeInput
            internalOrderBlocksBoxes.push(box.new(na,na,na,na,xloc = xloc.bar_time,extend = extend.right))

// @variable                        source to use in bearish order blocks mitigation
bearishOrderBlockMitigationSource   = orderBlockMitigationInput == CLOSE ? close : high
// @variable                        source to use in bullish order blocks mitigation
bullishOrderBlockMitigationSource   = orderBlockMitigationInput == CLOSE ? close : low
// @variable                        default volatility measure
atrMeasure                          = ta.atr(200)
// @variable                        parsed volatility measure by user settings
volatilityMeasure                   = orderBlockFilterInput == ATR ? atrMeasure : ta.cum(ta.tr)/bar_index
// @variable                        true if current bar is a high volatility bar
highVolatilityBar                   = (high - low) >= (2 * volatilityMeasure)
// @variable                        parsed high
parsedHigh                          = highVolatilityBar ? low : high
// @variable                        parsed low
parsedLow                           = highVolatilityBar ? high : low

// we store current values into the arrays at each bar
parsedHighs.push(parsedHigh)
parsedLows.push(parsedLow)
highs.push(high)
lows.push(low)
times.push(time)


// @function            Get the value of the current leg, it can be 0 (bearish) or 1 (bullish)
// @returns             int
leg(int size) =>
    var leg     = 0    
    newLegHigh  = high[size] > ta.highest( size)
    newLegLow   = low[size]  < ta.lowest(  size)
    
    if newLegHigh
        leg := BEARISH_LEG
    else if newLegLow
        leg := BULLISH_LEG
    leg

// @function            Identify whether the current value is the start of a new leg (swing)
// @param leg           (int) Current leg value
// @returns             bool
startOfNewLeg(int leg)      => ta.change(leg) != 0

// @function            Identify whether the current level is the start of a new bearish leg (swing)
// @param leg           (int) Current leg value
// @returns             bool
startOfBearishLeg(int leg)  => ta.change(leg) == -1

// @function            Identify whether the current level is the start of a new bullish leg (swing)
// @param leg           (int) Current leg value
// @returns             bool
startOfBullishLeg(int leg)  => ta.change(leg) == +1

// @function            create a new label
// @param labelTime     bar time coordinate
// @param labelPrice    price coordinate
// @param tag           text to display
// @param labelColor    text color
// @param labelStyle    label style
// @returns             label ID
drawLabel(int labelTime, float labelPrice, string tag, color labelColor, string labelStyle) =>    
    var label l_abel = na

    if modeInput == PRESENT
        l_abel.delete()

    l_abel := label.new(chart.point.new(labelTime,na,labelPrice),tag,xloc.bar_time,color=color(na),textcolor=labelColor,style = labelStyle,size = size.small)

// @function            create a new line and label representing an EQH or EQL
// @param p_ivot        starting pivot
// @param level         price level of current pivot
// @param size          how many bars ago was the current pivot detected
// @param equalHigh     true for EQH, false for EQL
// @returns             label ID
drawEqualHighLow(pivot p_ivot, float level, int size, bool equalHigh) =>
    equalDisplay e_qualDisplay = equalHigh ? equalHighDisplay : equalLowDisplay
    
    string tag          = 'EQL'
    color equalColor    = swingBullishColor
    string labelStyle   = label.style_label_up

    if equalHigh
        tag         := 'EQH'
        equalColor  := swingBearishColor
        labelStyle  := label.style_label_down

    if modeInput == PRESENT
        line.delete(    e_qualDisplay.l_ine)
        label.delete(   e_qualDisplay.l_abel)
        
    e_qualDisplay.l_ine     := line.new(chart.point.new(p_ivot.barTime,na,p_ivot.currentLevel), chart.point.new(time[size],na,level), xloc = xloc.bar_time, color = equalColor, style = line.style_dotted)
    labelPosition           = math.round(0.5*(p_ivot.barIndex + bar_index - size))
    e_qualDisplay.l_abel    := label.new(chart.point.new(na,labelPosition,level), tag, xloc.bar_index, color = color(na), textcolor = equalColor, style = labelStyle, size = equalHighsLowsSizeInput)

// @function            store current structure and trailing swing points, and also display swing points and equal highs/lows
// @param size          (int) structure size
// @param equalHighLow  (bool) true for displaying current highs/lows
// @param internal      (bool) true for getting internal structures
// @returns             label ID
getCurrentStructure(int size,bool equalHighLow = false, bool internal = false) =>        
    currentLeg              = leg(size)
    newPivot                = startOfNewLeg(currentLeg)
    pivotLow                = startOfBullishLeg(currentLeg)
    pivotHigh               = startOfBearishLeg(currentLeg)

    if newPivot
        if pivotLow
            pivot p_ivot    = equalHighLow ? equalLow : internal ? internalLow : swingLow    

            if equalHighLow and math.abs(p_ivot.currentLevel - low[size]) < equalHighsLowsThresholdInput * atrMeasure                
                drawEqualHighLow(p_ivot, low[size], size, false)

            p_ivot.lastLevel    := p_ivot.currentLevel
            p_ivot.currentLevel := low[size]
            p_ivot.crossed      := false
            p_ivot.barTime      := time[size]
            p_ivot.barIndex     := bar_index[size]

            if not equalHighLow and not internal
                trailing.bottom         := p_ivot.currentLevel
                trailing.barTime        := p_ivot.barTime
                trailing.barIndex       := p_ivot.barIndex
                trailing.lastBottomTime := p_ivot.barTime

            if showSwingsInput and not internal and not equalHighLow
                drawLabel(time[size], p_ivot.currentLevel, p_ivot.currentLevel < p_ivot.lastLevel ? 'LL' : 'HL', swingBullishColor, label.style_label_up)            
        else
            pivot p_ivot = equalHighLow ? equalHigh : internal ? internalHigh : swingHigh

            if equalHighLow and math.abs(p_ivot.currentLevel - high[size]) < equalHighsLowsThresholdInput * atrMeasure
                drawEqualHighLow(p_ivot,high[size],size,true)                

            p_ivot.lastLevel    := p_ivot.currentLevel
            p_ivot.currentLevel := high[size]
            p_ivot.crossed      := false
            p_ivot.barTime      := time[size]
            p_ivot.barIndex     := bar_index[size]

            if not equalHighLow and not internal
                trailing.top            := p_ivot.currentLevel
                trailing.barTime        := p_ivot.barTime
                trailing.barIndex       := p_ivot.barIndex
                trailing.lastTopTime    := p_ivot.barTime

            if showSwingsInput and not internal and not equalHighLow
                drawLabel(time[size], p_ivot.currentLevel, p_ivot.currentLevel > p_ivot.lastLevel ? 'HH' : 'LH', swingBearishColor, label.style_label_down)
                
// @function                draw line and label representing a structure
// @param p_ivot            base pivot point
// @param tag               test to display
// @param structureColor    base color
// @param lineStyle         line style
// @param labelStyle        label style
// @param labelSize         text size
// @returns                 label ID
drawStructure(pivot p_ivot, string tag, color structureColor, string lineStyle, string labelStyle, string labelSize) =>    
    var line l_ine      = line.new(na,na,na,na,xloc = xloc.bar_time)
    var label l_abel    = label.new(na,na)

    if modeInput == PRESENT
        l_ine.delete()
        l_abel.delete()

    l_ine   := line.new(chart.point.new(p_ivot.barTime,na,p_ivot.currentLevel), chart.point.new(time,na,p_ivot.currentLevel), xloc.bar_time, color=structureColor, style=lineStyle)
    l_abel  := label.new(chart.point.new(na,math.round(0.5*(p_ivot.barIndex+bar_index)),p_ivot.currentLevel), tag, xloc.bar_index, color=color(na), textcolor=structureColor, style=labelStyle, size = labelSize)

// @function            delete order blocks
// @param internal      true for internal order blocks
// @returns             orderBlock ID
deleteOrderBlocks(bool internal = false) =>
    array<orderBlock> orderBlocks = internal ? internalOrderBlocks : swingOrderBlocks

    for [index,eachOrderBlock] in orderBlocks
        bool crossedOderBlock = false
        
        if bearishOrderBlockMitigationSource > eachOrderBlock.barHigh and eachOrderBlock.bias == BEARISH
            crossedOderBlock := true
            if internal
                currentAlerts.internalBearishOrderBlock := true
            else
                currentAlerts.swingBearishOrderBlock    := true
        else if bullishOrderBlockMitigationSource < eachOrderBlock.barLow and eachOrderBlock.bias == BULLISH
            crossedOderBlock := true
            if internal
                currentAlerts.internalBullishOrderBlock := true
            else
                currentAlerts.swingBullishOrderBlock    := true
        if crossedOderBlock                    
            orderBlocks.remove(index)            

// @function            fetch and store order blocks
// @param p_ivot        base pivot point
// @param internal      true for internal order blocks
// @param bias          BULLISH or BEARISH
// @returns             void
storeOrdeBlock(pivot p_ivot,bool internal = false,int bias) =>
    if (not internal and showSwingOrderBlocksInput) or (internal and showInternalOrderBlocksInput)

        array<float> a_rray = na
        int parsedIndex = na

        if bias == BEARISH
            a_rray      := parsedHighs.slice(p_ivot.barIndex,bar_index)
            parsedIndex := p_ivot.barIndex + a_rray.indexof(a_rray.max())  
        else
            a_rray      := parsedLows.slice(p_ivot.barIndex,bar_index)
            parsedIndex := p_ivot.barIndex + a_rray.indexof(a_rray.min())                        

        orderBlock o_rderBlock          = orderBlock.new(parsedHighs.get(parsedIndex), parsedLows.get(parsedIndex), times.get(parsedIndex),bias)
        array<orderBlock> orderBlocks   = internal ? internalOrderBlocks : swingOrderBlocks
        
        if orderBlocks.size() >= 100
            orderBlocks.pop()
        orderBlocks.unshift(o_rderBlock)

// @function            draw order blocks as boxes
// @param internal      true for internal order blocks
// @returns             void
drawOrderBlocks(bool internal = false) =>        
    array<orderBlock> orderBlocks = internal ? internalOrderBlocks : swingOrderBlocks
    orderBlocksSize = orderBlocks.size()

    if orderBlocksSize > 0        
        maxOrderBlocks                      = internal ? internalOrderBlocksSizeInput : swingOrderBlocksSizeInput
        array<orderBlock> parsedOrdeBlocks  = orderBlocks.slice(0, math.min(maxOrderBlocks,orderBlocksSize))
        array<box> b_oxes                   = internal ? internalOrderBlocksBoxes : swingOrderBlocksBoxes        

        for [index,eachOrderBlock] in parsedOrdeBlocks
            orderBlockColor = styleInput == MONOCHROME ? (eachOrderBlock.bias == BEARISH ? color.new(MONO_BEARISH,80) : color.new(MONO_BULLISH,80)) : internal ? (eachOrderBlock.bias == BEARISH ? internalBearishOrderBlockColor : internalBullishOrderBlockColor) : (eachOrderBlock.bias == BEARISH ? swingBearishOrderBlockColor : swingBullishOrderBlockColor)

            box b_ox        = b_oxes.get(index)
            b_ox.set_top_left_point(    chart.point.new(eachOrderBlock.barTime,na,eachOrderBlock.barHigh))
            b_ox.set_bottom_right_point(chart.point.new(last_bar_time,na,eachOrderBlock.barLow))        
            b_ox.set_border_color(      internal ? na : orderBlockColor)
            b_ox.set_bgcolor(           orderBlockColor)

// @function            detect and draw structures, also detect and store order blocks
// @param internal      true for internal structures or order blocks
// @returns             void
displayStructure(bool internal = false) =>
    var bullishBar = true
    var bearishBar = true

    if internalFilterConfluenceInput
        bullishBar := high - math.max(close, open) > math.min(close, open - low)
        bearishBar := high - math.max(close, open) < math.min(close, open - low)
    
    pivot p_ivot    = internal ? internalHigh : swingHigh
    trend t_rend    = internal ? internalTrend : swingTrend

    lineStyle       = internal ? line.style_dashed : line.style_solid
    labelSize       = internal ? internalStructureSize : swingStructureSize

    extraCondition  = internal ? internalHigh.currentLevel != swingHigh.currentLevel and bullishBar : true
    bullishColor    = styleInput == MONOCHROME ? MONO_BULLISH : internal ? internalBullColorInput : swingBullColorInput

    if ta.crossover(close,p_ivot.currentLevel) and not p_ivot.crossed and extraCondition
        string tag = t_rend.bias == BEARISH ? CHOCH : BOS

        if internal
            currentAlerts.internalBullishCHoCH  := tag == CHOCH
            currentAlerts.internalBullishBOS    := tag == BOS
        else
            currentAlerts.swingBullishCHoCH     := tag == CHOCH
            currentAlerts.swingBullishBOS       := tag == BOS

        p_ivot.crossed  := true
        t_rend.bias     := BULLISH

        displayCondition = internal ? showInternalsInput and (showInternalBullInput == ALL or (showInternalBullInput == BOS and tag != CHOCH) or (showInternalBullInput == CHOCH and tag == CHOCH)) : showStructureInput and (showSwingBullInput == ALL or (showSwingBullInput == BOS and tag != CHOCH) or (showSwingBullInput == CHOCH and tag == CHOCH))

        if displayCondition                        
            drawStructure(p_ivot,tag,bullishColor,lineStyle,label.style_label_down,labelSize)

        if (internal and showInternalOrderBlocksInput) or (not internal and showSwingOrderBlocksInput)
            storeOrdeBlock(p_ivot,internal,BULLISH)

    p_ivot          := internal ? internalLow : swingLow    
    extraCondition  := internal ? internalLow.currentLevel != swingLow.currentLevel and bearishBar : true
    bearishColor    = styleInput == MONOCHROME ? MONO_BEARISH : internal ? internalBearColorInput : swingBearColorInput

    if ta.crossunder(close,p_ivot.currentLevel) and not p_ivot.crossed and extraCondition
        string tag = t_rend.bias == BULLISH ? CHOCH : BOS

        if internal
            currentAlerts.internalBearishCHoCH  := tag == CHOCH
            currentAlerts.internalBearishBOS    := tag == BOS
        else
            currentAlerts.swingBearishCHoCH     := tag == CHOCH
            currentAlerts.swingBearishBOS       := tag == BOS

        p_ivot.crossed := true
        t_rend.bias := BEARISH

        displayCondition = internal ? showInternalsInput and (showInternalBearInput == ALL or (showInternalBearInput == BOS and tag != CHOCH) or (showInternalBearInput == CHOCH and tag == CHOCH)) : showStructureInput and (showSwingBearInput == ALL or (showSwingBearInput == BOS and tag != CHOCH) or (showSwingBearInput == CHOCH and tag == CHOCH))
        
        if displayCondition                        
            drawStructure(p_ivot,tag,bearishColor,lineStyle,label.style_label_up,labelSize)

        if (internal and showInternalOrderBlocksInput) or (not internal and showSwingOrderBlocksInput)
            storeOrdeBlock(p_ivot,internal,BEARISH)

// @function            draw one fair value gap box (each fair value gap has two boxes)
// @param leftTime      left time coordinate
// @param rightTime     right time coordinate
// @param topPrice      top price level
// @param bottomPrice   bottom price level
// @param boxColor      box color
// @returns             box ID
fairValueGapBox(leftTime,rightTime,topPrice,bottomPrice,boxColor) => box.new(chart.point.new(leftTime,na,topPrice),chart.point.new(rightTime + fairValueGapsExtendInput * (time-time[1]),na,bottomPrice), xloc=xloc.bar_time, border_color = boxColor, bgcolor = boxColor)

// @function            delete fair value gaps
// @returns             fairValueGap ID
deleteFairValueGaps() =>
    for [index,eachFairValueGap] in fairValueGaps
        if (low < eachFairValueGap.bottom and eachFairValueGap.bias == BULLISH) or (high > eachFairValueGap.top and eachFairValueGap.bias == BEARISH)
            eachFairValueGap.topBox.delete()
            eachFairValueGap.bottomBox.delete()
            fairValueGaps.remove(index)
    
// @function            draw fair value gaps
// @returns             fairValueGap ID
drawFairValueGaps() => 
    [lastClose, lastOpen, lastTime, currentHigh, currentLow, currentTime, last2High, last2Low] = request.security(syminfo.tickerid, fairValueGapsTimeframeInput, [close[1], open[1], time[1], high[0], low[0], time[0], high[2], low[2]],lookahead = barmerge.lookahead_on)

    barDeltaPercent     = (lastClose - lastOpen) / (lastOpen * 100)
    newTimeframe        = timeframe.change(fairValueGapsTimeframeInput)
    threshold           = fairValueGapsThresholdInput ? ta.cum(math.abs(newTimeframe ? barDeltaPercent : 0)) / bar_index * 2 : 0

    bullishFairValueGap = currentLow > last2High and lastClose > last2High and barDeltaPercent > threshold and newTimeframe
    bearishFairValueGap = currentHigh < last2Low and lastClose < last2Low and -barDeltaPercent > threshold and newTimeframe

    if bullishFairValueGap
        currentAlerts.bullishFairValueGap := true
        fairValueGaps.unshift(fairValueGap.new(currentLow,last2High,BULLISH,fairValueGapBox(lastTime,currentTime,currentLow,math.avg(currentLow,last2High),fairValueGapBullishColor),fairValueGapBox(lastTime,currentTime,math.avg(currentLow,last2High),last2High,fairValueGapBullishColor)))
    if bearishFairValueGap
        currentAlerts.bearishFairValueGap := true
        fairValueGaps.unshift(fairValueGap.new(currentHigh,last2Low,BEARISH,fairValueGapBox(lastTime,currentTime,currentHigh,math.avg(currentHigh,last2Low),fairValueGapBearishColor),fairValueGapBox(lastTime,currentTime,math.avg(currentHigh,last2Low),last2Low,fairValueGapBearishColor)))

// @function            get line style from string
// @param style         line style
// @returns             string
getStyle(string style) =>
    switch style
        SOLID => line.style_solid
        DASHED => line.style_dashed
        DOTTED => line.style_dotted

// @function            draw MultiTimeFrame levels
// @param timeframe     base timeframe
// @param sameTimeframe true if chart timeframe is same as base timeframe
// @param style         line style
// @param levelColor    line and text color
// @returns             void
drawLevels(string timeframe, bool sameTimeframe, string style, color levelColor) =>
    [topLevel, bottomLevel, leftTime, rightTime] = request.security(syminfo.tickerid, timeframe, [high[1], low[1], time[1], time],lookahead = barmerge.lookahead_on)

    float parsedTop         = sameTimeframe ? high : topLevel
    float parsedBottom      = sameTimeframe ? low : bottomLevel    

    int parsedLeftTime      = sameTimeframe ? time : leftTime
    int parsedRightTime     = sameTimeframe ? time : rightTime

    int parsedTopTime       = time
    int parsedBottomTime    = time

    if not sameTimeframe
        int leftIndex               = times.binary_search_rightmost(parsedLeftTime)
        int rightIndex              = times.binary_search_rightmost(parsedRightTime)

        array<int> timeArray        = times.slice(leftIndex,rightIndex)
        array<float> topArray       = highs.slice(leftIndex,rightIndex)
        array<float> bottomArray    = lows.slice(leftIndex,rightIndex)

        parsedTopTime               := timeArray.size() > 0 ? timeArray.get(topArray.indexof(topArray.max())) : initialTime
        parsedBottomTime            := timeArray.size() > 0 ? timeArray.get(bottomArray.indexof(bottomArray.min())) : initialTime

    var line topLine        = line.new(na, na, na, na, xloc = xloc.bar_time, color = levelColor, style = getStyle(style))
    var line bottomLine     = line.new(na, na, na, na, xloc = xloc.bar_time, color = levelColor, style = getStyle(style))
    var label topLabel      = label.new(na, na, xloc = xloc.bar_time, text = str.format('P{0}H',timeframe), color=color(na), textcolor = levelColor, size = size.small, style = label.style_label_left)
    var label bottomLabel   = label.new(na, na, xloc = xloc.bar_time, text = str.format('P{0}L',timeframe), color=color(na), textcolor = levelColor, size = size.small, style = label.style_label_left)

    topLine.set_first_point(    chart.point.new(parsedTopTime,na,parsedTop))
    topLine.set_second_point(   chart.point.new(last_bar_time + 20 * (time-time[1]),na,parsedTop))   
    topLabel.set_point(         chart.point.new(last_bar_time + 20 * (time-time[1]),na,parsedTop))

    bottomLine.set_first_point( chart.point.new(parsedBottomTime,na,parsedBottom))    
    bottomLine.set_second_point(chart.point.new(last_bar_time + 20 * (time-time[1]),na,parsedBottom))
    bottomLabel.set_point(      chart.point.new(last_bar_time + 20 * (time-time[1]),na,parsedBottom))

// @function            true if chart timeframe is higher than provided timeframe
// @param timeframe     timeframe to check
// @returns             bool
higherTimeframe(string timeframe) => timeframe.in_seconds() > timeframe.in_seconds(timeframe)

// @function            update trailing swing points
// @returns             int
updateTrailingExtremes() =>
    trailing.top            := math.max(high,trailing.top)
    trailing.lastTopTime    := trailing.top == high ? time : trailing.lastTopTime
    trailing.bottom         := math.min(low,trailing.bottom)
    trailing.lastBottomTime := trailing.bottom == low ? time : trailing.lastBottomTime

// @function            draw trailing swing points
// @returns             void
drawHighLowSwings() =>
    var line topLine        = line.new(na, na, na, na, color = swingBearishColor, xloc = xloc.bar_time)
    var line bottomLine     = line.new(na, na, na, na, color = swingBullishColor, xloc = xloc.bar_time)
    var label topLabel      = label.new(na, na, color=color(na), textcolor = swingBearishColor, xloc = xloc.bar_time, style = label.style_label_down, size = size.tiny)
    var label bottomLabel   = label.new(na, na, color=color(na), textcolor = swingBullishColor, xloc = xloc.bar_time, style = label.style_label_up, size = size.tiny)

    rightTimeBar            = last_bar_time + 20 * (time - time[1])

    topLine.set_first_point(    chart.point.new(trailing.lastTopTime, na, trailing.top))
    topLine.set_second_point(   chart.point.new(rightTimeBar, na, trailing.top))
    topLabel.set_point(         chart.point.new(rightTimeBar, na, trailing.top))
    topLabel.set_text(          swingTrend.bias == BEARISH ? 'Strong High' : 'Weak High')

    bottomLine.set_first_point( chart.point.new(trailing.lastBottomTime, na, trailing.bottom))
    bottomLine.set_second_point(chart.point.new(rightTimeBar, na, trailing.bottom))
    bottomLabel.set_point(      chart.point.new(rightTimeBar, na, trailing.bottom))
    bottomLabel.set_text(       swingTrend.bias == BULLISH ? 'Strong Low' : 'Weak Low')

// @function            draw a zone with a label and a box
// @param labelLevel    price level for label
// @param labelIndex    bar index for label
// @param top           top price level for box
// @param bottom        bottom price level for box
// @param tag           text to display
// @param zoneColor     base color
// @param style         label style
// @returns             void
drawZone(float labelLevel, int labelIndex, float top, float bottom, string tag, color zoneColor, string style) =>
    var label l_abel    = label.new(na,na,text = tag, color=color(na),textcolor = zoneColor, style = style, size = size.small)
    var box b_ox        = box.new(na,na,na,na,bgcolor = color.new(zoneColor,80),border_color = color(na), xloc = xloc.bar_time)

    b_ox.set_top_left_point(    chart.point.new(trailing.barTime,na,top))
    b_ox.set_bottom_right_point(chart.point.new(last_bar_time,na,bottom))

    l_abel.set_point(           chart.point.new(na,labelIndex,labelLevel))

// @function            draw premium/discount zones
// @returns             void
drawPremiumDiscountZones() =>
    drawZone(trailing.top, math.round(0.5*(trailing.barIndex + last_bar_index)), trailing.top, 0.95*trailing.top + 0.05*trailing.bottom, 'Premium', premiumZoneColor, label.style_label_down)

    equilibriumLevel = math.avg(trailing.top, trailing.bottom)
    drawZone(equilibriumLevel, last_bar_index, 0.525*trailing.top + 0.475*trailing.bottom, 0.525*trailing.bottom + 0.475*trailing.top, 'Equilibrium', equilibriumZoneColorInput, label.style_label_left)

    drawZone(trailing.bottom, math.round(0.5*(trailing.barIndex + last_bar_index)), 0.95*trailing.bottom + 0.05*trailing.top, trailing.bottom, 'Discount', discountZoneColor, label.style_label_up)


parsedOpen  = showTrendInput ? open : na
candleColor = internalTrend.bias == BULLISH ? swingBullishColor : swingBearishColor
plotcandle(parsedOpen,high,low,close,color = candleColor, wickcolor = candleColor, bordercolor = candleColor)

if showHighLowSwingsInput or showPremiumDiscountZonesInput
    updateTrailingExtremes()

    if showHighLowSwingsInput
        drawHighLowSwings()

    if showPremiumDiscountZonesInput
        drawPremiumDiscountZones()

if showFairValueGapsInput
    deleteFairValueGaps()

getCurrentStructure(swingsLengthInput,false)
getCurrentStructure(5,false,true)

if showEqualHighsLowsInput
    getCurrentStructure(equalHighsLowsLengthInput,true)

if showInternalsInput or showInternalOrderBlocksInput or showTrendInput
    displayStructure(true)

if showStructureInput or showSwingOrderBlocksInput or showHighLowSwingsInput
    displayStructure()

if showInternalOrderBlocksInput
    deleteOrderBlocks(true)

if showSwingOrderBlocksInput
    deleteOrderBlocks()

if showFairValueGapsInput
    drawFairValueGaps()

if barstate.islastconfirmedhistory or barstate.islast
    if showInternalOrderBlocksInput        
        drawOrderBlocks(true)
        
    if showSwingOrderBlocksInput        
        drawOrderBlocks()

lastBarIndex    := currentBarIndex
currentBarIndex := bar_index
newBar          = currentBarIndex != lastBarIndex

if barstate.islastconfirmedhistory or (barstate.isrealtime and newBar)
    if showDailyLevelsInput and not higherTimeframe('D')
        drawLevels('D',timeframe.isdaily,dailyLevelsStyleInput,dailyLevelsColorInput)

    if showWeeklyLevelsInput and not higherTimeframe('W')
        drawLevels('W',timeframe.isweekly,weeklyLevelsStyleInput,weeklyLevelsColorInput)

    if showMonthlyLevelsInput and not higherTimeframe('M')
        drawLevels('M',timeframe.ismonthly,monthlyLevelsStyleInput,monthlyLevelsColorInput)


alertcondition(currentAlerts.internalBullishBOS,        'Internal Bullish BOS',         'Internal Bullish BOS formed')
alertcondition(currentAlerts.internalBullishCHoCH,      'Internal Bullish CHoCH',       'Internal Bullish CHoCH formed')
alertcondition(currentAlerts.internalBearishBOS,        'Internal Bearish BOS',         'Internal Bearish BOS formed')
alertcondition(currentAlerts.internalBearishCHoCH,      'Internal Bearish CHoCH',       'Internal Bearish CHoCH formed')

alertcondition(currentAlerts.swingBullishBOS,           'Bullish BOS',                  'Internal Bullish BOS formed')
alertcondition(currentAlerts.swingBullishCHoCH,         'Bullish CHoCH',                'Internal Bullish CHoCH formed')
alertcondition(currentAlerts.swingBearishBOS,           'Bearish BOS',                  'Bearish BOS formed')
alertcondition(currentAlerts.swingBearishCHoCH,         'Bearish CHoCH',                'Bearish CHoCH formed')

alertcondition(currentAlerts.internalBullishOrderBlock, 'Bullish Internal OB Breakout', 'Price broke bullish internal OB')
alertcondition(currentAlerts.internalBearishOrderBlock, 'Bearish Internal OB Breakout', 'Price broke bearish internal OB')
alertcondition(currentAlerts.swingBullishOrderBlock,    'Bullish Swing OB Breakout',    'Price broke bullish swing OB')
alertcondition(currentAlerts.swingBearishOrderBlock,    'Bearish Swing OB Breakout',    'Price broke bearish swing OB')

alertcondition(currentAlerts.equalHighs,                'Equal Highs',                  'Equal highs detected')
alertcondition(currentAlerts.equalLows,                 'Equal Lows',                   'Equal lows detected')

alertcondition(currentAlerts.bullishFairValueGap,       'Bullish FVG',                  'Bullish FVG formed')
alertcondition(currentAlerts.bearishFairValueGap,       'Bearish FVG',                  'Bearish FVG formed')

