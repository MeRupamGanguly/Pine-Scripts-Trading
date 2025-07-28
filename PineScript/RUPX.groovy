// This Pine Script® code is subject to the terms of the Mozilla Public License 2.0 at https://mozilla.org/MPL/2.0/
// © Rupam-G

//@version=6
indicator("RUPX", overlay=true,max_lines_count=500, max_labels_count=500)

// ———— Color Scheme ————

colorBB = #FFFFFF
colorBB1H = #ffcc00ff
colorBB4H = #7CFC00
colorBB1D = #FF6347
colorText = #E0E0E0

// ———— EMA Colors ————
color200EMA    = input.color(color.rgb(51, 8, 151), "Color: 200 EMA", group="200 EMA Settings")
color2001HEMA  = input.color(#557dd3, "Color: 1H 200 EMA", group="200 EMA Settings")
color2004HEMA  = input.color(#5AC8FA, "Color: 4H 200 EMA", group="200 EMA Settings")

// ———— EMA Visibility Toggles ————
show200EMA     = input.bool(true, "Show 200 EMA", group="200 EMA Settings")
show2001HEMA   = input.bool(true, "Show 1H 200 EMA", group="200 EMA Settings")
show2004HEMA   = input.bool(true, "Show 4H 200 EMA", group="200 EMA Settings")


// ———— Bollinger Bands Settings ————
showBB = input.bool(true, "Show BB", group="Bollinger Bands")
showBB1H = input.bool(true, "Show 1H BB", group="Bollinger Bands")
showBB4H = input.bool(true, "Show 4H BB", group="Bollinger Bands")
showBB1D = input.bool(true, "Show 1D BB", group="Bollinger Bands")
bbLength = input.int(20, "BB Length", group="Bollinger Bands")
bbMultiplier = input.float(2.0, "BB Multiplier", group="Bollinger Bands")

// ———— RSI Settings ————
rsiLength = input.int(14, title="RSI Length", group="RSI Settings")
rsiOverbought = input.int(70, title="RSI Overbought Level", group="RSI Settings")
rsiOversold = input.int(30, title="RSI Oversold Level", group="RSI Settings")
ShowTable = input.bool(true, title="Show Table for Multi-Timeframe", group="RSI Settings")

// ———— General Box Settings ————
max_days = input.int(15, "Max Boxes to Show", minval=1, group="Global Kill Zone Settings")
box_transparency = input.int(80, "Default Box Transparency", 0, 100, group="Global Kill Zone Settings")
text_color = input.color(color.white, "Text Color", group="Global Kill Zone Settings")

// ———— Asia Kill Zone ————
show_asia = input.bool(true, "Enable Asia Kill Zone", group="Asia Session")
toggle_asia = input.bool(true, "Show Box", group="Asia Session")
asia_session = input.session("0530-0925", "Session Time", group="Asia Session")
asia_color = input.color(color.blue, "Session Color", group="Asia Session")
asia_transparency = input.int(80, "Box Transparency", 0, 100, group="Asia Session")

// ———— London Kill Zone ————
show_london = input.bool(true, "Enable London Kill Zone", group="London Session")
toggle_london = input.bool(true, "Show Box", group="London Session")
london_session = input.session("1130-1425", "Session Time", group="London Session")
london_color = input.color(color.red, "Session Color", group="London Session")
london_transparency = input.int(80, "Box Transparency", 0, 100, group="London Session")

// ———— New York Kill Zones ————
ny_base_color = input.color(color.purple, "NY Session Shared Color", group="New York Sessions")

show_ny_am = input.bool(true, "Enable NY AM", group="New York Sessions")
toggle_ny_am = input.bool(true, "Show AM Box", group="New York Sessions")
ny_am_session = input.session("1900-2025", "AM Session Time", group="New York Sessions")
ny_am_transparency = input.int(70, "AM Box Transparency", 0, 100, group="New York Sessions")

show_ny_lunch = input.bool(true, "Enable NY Lunch", group="New York Sessions")
toggle_ny_lunch = input.bool(true, "Show Lunch Box", group="New York Sessions")
ny_lunch_session = input.session("2130-2225", "Lunch Session Time", group="New York Sessions")
ny_lunch_transparency = input.int(85, "Lunch Box Transparency", 0, 100, group="New York Sessions")

show_ny_pm = input.bool(true, "Enable NY PM", group="New York Sessions")
toggle_ny_pm = input.bool(true, "Show PM Box", group="New York Sessions")
ny_pm_session = input.session("2300-0125", "PM Session Time", group="New York Sessions")
ny_pm_transparency = input.int(70, "PM Box Transparency", 0, 100, group="New York Sessions")


src = close

// ———— EMA Calculations ————
ema200         = ta.ema(close, 200)
ema200_1H      = request.security(syminfo.tickerid, "60", ta.ema(close, 200))
ema200_4H      = request.security(syminfo.tickerid, "240", ta.ema(close, 200))

// ———— EMA Plotting ————
plot(show200EMA ? ema200 : na, title="200 EMA", color=color200EMA, linewidth=2)
plot(show2001HEMA ? ema200_1H : na, title="1H 200 EMA", color=color2001HEMA, linewidth=2)
plot(show2004HEMA ? ema200_4H : na, title="4H 200 EMA", color=color2004HEMA, linewidth=2)

// ———— BB Calculations ————
bb_basis = ta.sma(src, bbLength)
bb_dev = bbMultiplier * ta.stdev(src, bbLength)
bb_upper = bb_basis + bb_dev
bb_lower = bb_basis - bb_dev

[bb1H_basis, bb1H_dev] = request.security(syminfo.tickerid, "60", [ta.sma(src, bbLength), bbMultiplier * ta.stdev(src, bbLength)])
[bb4H_basis, bb4H_dev] = request.security(syminfo.tickerid, "240", [ta.sma(src, bbLength), bbMultiplier * ta.stdev(src, bbLength)])
[bb1D_basis, bb1D_dev] = request.security(syminfo.tickerid, "D", [ta.sma(src, bbLength), bbMultiplier * ta.stdev(src, bbLength)])

// ———— BB Plots ————
plot(showBB ? bb_upper : na, "BB Upper", color.new(colorBB, 30))
plot(showBB ? bb_basis : na, "BB Basis", color.new(colorBB, 30))
plot(showBB ? bb_lower : na, "BB Lower", color.new(colorBB, 30))

plot(showBB1H ? bb1H_basis + bb1H_dev : na, "BB 1H Upper", color.new(colorBB1H, 40))
plot(showBB1H ? bb1H_basis : na, "BB 1H Mid", color.new(colorBB1H, 40))
plot(showBB1H ? bb1H_basis - bb1H_dev : na, "BB 1H Lower", color.new(colorBB1H, 40))

plot(showBB4H ? bb4H_basis + bb4H_dev : na, "BB 4H Upper", color.new(colorBB4H, 40))
plot(showBB4H ? bb4H_basis : na, "BB 4H Mid", color.new(colorBB4H, 40))
plot(showBB4H ? bb4H_basis - bb4H_dev : na, "BB 4H Lower", color.new(colorBB4H, 40))

plot(showBB1D ? bb1D_basis + bb1D_dev : na, "BB 1D Upper", color.new(colorBB1D, 40))
plot(showBB1D ? bb1D_basis : na, "BB 1D Mid", color.new(colorBB1D, 40))
plot(showBB1D ? bb1D_basis - bb1D_dev : na, "BB 1D Lower", color.new(colorBB1D, 40))

// ———— RSI Calculation ————
rsiValue = ta.rsi(close, rsiLength)
rsiOverboughtCondition = rsiValue > rsiOverbought
rsiOversoldCondition = rsiValue < rsiOversold

// ———— Background Color for RSI Pane ————
bgcolor(rsiOverboughtCondition ? color.new(color.red, 90) : na)
bgcolor(rsiOversoldCondition ? color.new(color.green, 90) : na)

gettext(bull, bear, iv) =>
    str.tostring(iv, format.mintick)

getcolor(bull, bear) =>
    bull ? color.green : bear ? color.red : color.rgb(0, 0, 0, 90)

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

// ———— Show Table ————
if ShowTable
    tblRSI = table.new(position.top_right, 7, 2, bgcolor=color.rgb(0, 0, 0, 90), border_color=color.navy, border_width=1)

    tblRSI.cell(0, 0, "IND", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRSI.cell(1, 0, "1M", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRSI.cell(2, 0, "5M", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRSI.cell(3, 0, "15M", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRSI.cell(4, 0, "1H", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRSI.cell(5, 0, "4H", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)
    tblRSI.cell(6, 0, "1D", bgcolor=color.rgb(0, 0, 0, 90), text_color=color.white)

    isBullish = rsiValue > rsiOverbought
    isBearish = rsiValue < rsiOversold

    addcell(tblRSI, 1, "RSI", isBullish, isBearish, rsiValue)



// ———— Kill Zone Logic ————
manage_boxes(show, session, box_color) =>
    var boxes = array.new_box()
    in_session = not na(time(timeframe.period, session, "Asia/Kolkata"))

    if show
        if in_session and not in_session[1]
            new_box = box.new(na, na, na, na, xloc=xloc.bar_time)
            array.unshift(boxes, new_box)
            box.set_left(new_box, time)
            box.set_top(new_box, high)
            box.set_bottom(new_box, low)
            box.set_bgcolor(new_box, box_color)
            box.set_border_color(new_box, box_color)

        if array.size(boxes) > 0
            current_box = array.get(boxes, 0)
            if in_session
                box.set_right(current_box, time)
                box.set_top(current_box, math.max(box.get_top(current_box), high))
                box.set_bottom(current_box, math.min(box.get_bottom(current_box), low))
            if array.size(boxes) > max_days
                box.delete(array.pop(boxes))

// ———— Draw Kill Zone Boxes ————
manage_boxes(show_asia and toggle_asia, asia_session, color.new(asia_color, asia_transparency))
manage_boxes(show_london and toggle_london, london_session, color.new(london_color, london_transparency))
manage_boxes(show_ny_am and toggle_ny_am, ny_am_session, color.new(ny_base_color, ny_am_transparency))
manage_boxes(show_ny_lunch and toggle_ny_lunch, ny_lunch_session, color.new(ny_base_color, ny_lunch_transparency))
manage_boxes(show_ny_pm and toggle_ny_pm, ny_pm_session, color.new(ny_base_color, ny_pm_transparency))

// ———— FVG BOS CHOCK HIGHs LOWs ————
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
showFairValueGapsInput          = input(        true,      'Fair Value Gaps',          group = GAPS_GROUP,     tooltip = showFairValueGapsTooltip)
fairValueGapsThresholdInput     = input(        true,       'Auto Threshold',           group = GAPS_GROUP,     tooltip = fairValueGapsThresholdTooltip)
fairValueGapsTimeframeInput     = input.timeframe('',       'Timeframe',                group = GAPS_GROUP,     tooltip = fairValueGapsTimeframeTooltip)
fairValueGapsBullColorInput     = input.color(color.new(#00ff68, 70), 'Bullish FVG' , group = GAPS_GROUP)
fairValueGapsBearColorInput     = input.color(color.new(#ff0008, 70), 'Bearish FVG' , group = GAPS_GROUP)
fairValueGapsExtendInput        = input.int(    4,          'Extend FVG',               group = GAPS_GROUP,     tooltip = fairValueGapsExtendTooltip,       minval = 0)
showDailyLevelsInput            = input(        false,      'Daily',    group = LEVELS_GROUP,   inline = 'daily')
dailyLevelsStyleInput           = input.string( SOLID,      '',         group = LEVELS_GROUP,   inline = 'daily',   options = [SOLID,DASHED,DOTTED])
dailyLevelsColorInput           = input(        BLUE,       '',         group = LEVELS_GROUP,   inline = 'daily')
showWeeklyLevelsInput           = input(        false,      'Weekly',   group = LEVELS_GROUP,   inline = 'weekly')
weeklyLevelsStyleInput          = input.string( SOLID,      '',         group = LEVELS_GROUP,   inline = 'weekly',  options = [SOLID,DASHED,DOTTED])
weeklyLevelsColorInput          = input(        BLUE,       '',         group = LEVELS_GROUP,   inline = 'weekly')
showMonthlyLevelsInput          = input(        false,      'Monthly',   group = LEVELS_GROUP,   inline = 'monthly')
monthlyLevelsStyleInput         = input.string( SOLID,      '',         group = LEVELS_GROUP,   inline = 'monthly', options = [SOLID,DASHED,DOTTED])
monthlyLevelsColorInput         = input(        BLUE,       '',         group = LEVELS_GROUP,   inline = 'monthly')

type trailingExtremes
    float top
    float bottom
    int barTime
    int barIndex
    int lastTopTime
    int lastBottomTime

type fairValueGap
    float top
    float bottom
    int bias
    box topBox
    box bottomBox

type trend
    int bias 

type equalDisplay
    line l_ine      = na
    label l_abel    = na

type pivot
    float currentLevel
    float lastLevel
    bool crossed
    int barTime     = time
    int barIndex    = bar_index

type orderBlock
    float barHigh
    float barLow
    int barTime    
    int bias

var pivot swingHigh = pivot.new(na, na, false)
var pivot swingLow = pivot.new(na, na, false)
var pivot internalHigh = pivot.new(na, na, false)
var pivot internalLow = pivot.new(na, na, false)
var pivot equalHigh = pivot.new(na, na, false)
var pivot equalLow = pivot.new(na, na, false)
var trend swingTrend = trend.new(0)
var trend internalTrend = trend.new(0)
var equalDisplay equalHighDisplay = equalDisplay.new()
var equalDisplay equalLowDisplay = equalDisplay.new()
var array<fairValueGap> fairValueGaps = array.new<fairValueGap>()
var array<float> parsedHighs = array.new<float>()
var array<float> parsedLows = array.new<float>()
var array<float> highs = array.new<float>()
var array<float> lows = array.new<float>()
var array<int> times = array.new<int>()
var trailingExtremes trailing = trailingExtremes.new()
var array<orderBlock> swingOrderBlocks = array.new<orderBlock>()
var array<orderBlock> internalOrderBlocks = array.new<orderBlock>()
var array<box> swingOrderBlocksBoxes = array.new<box>()
var array<box> internalOrderBlocksBoxes = array.new<box>()
var swingBullishColor = styleInput == MONOCHROME ? MONO_BULLISH : swingBullColorInput
var swingBearishColor = styleInput == MONOCHROME ? MONO_BEARISH : swingBearColorInput
var fairValueGapBullishColor = styleInput == MONOCHROME ? color.new(MONO_BULLISH, 70) : fairValueGapsBullColorInput
var fairValueGapBearishColor = styleInput == MONOCHROME ? color.new(MONO_BEARISH, 70) : fairValueGapsBearColorInput
varip int currentBarIndex = bar_index
varip int lastBarIndex = bar_index
var initialTime = time

// we create the needed boxes for displaying order blocks at the first execution
if barstate.isfirst
    if showSwingOrderBlocksInput
        for index = 1 to swingOrderBlocksSizeInput
            swingOrderBlocksBoxes.push(box.new(na,na,na,na,xloc = xloc.bar_time,extend = extend.right))
    if showInternalOrderBlocksInput
        for index = 1 to internalOrderBlocksSizeInput
            internalOrderBlocksBoxes.push(box.new(na,na,na,na,xloc = xloc.bar_time,extend = extend.right))

bearishOrderBlockMitigationSource = orderBlockMitigationInput == CLOSE ? close : high
bullishOrderBlockMitigationSource = orderBlockMitigationInput == CLOSE ? close : low

atrMeasure = ta.atr(200)
volatilityMeasure = orderBlockFilterInput == ATR ? atrMeasure : ta.cum(ta.tr) / bar_index
highVolatilityBar = (high - low) >= (2 * volatilityMeasure)
parsedHigh = highVolatilityBar ? low : high
parsedLow = highVolatilityBar ? high : low

// we store current values into the arrays at each bar
parsedHighs.push(parsedHigh)
parsedLows.push(parsedLow)
highs.push(high)
lows.push(low)
times.push(time)

// Get the value of the current leg, it can be 0 (bearish) or 1 (bullish)
leg(int size) =>
    var leg     = 0    
    newLegHigh  = high[size] > ta.highest( size)
    newLegLow   = low[size]  < ta.lowest(  size)
    
    if newLegHigh
        leg := BEARISH_LEG
    else if newLegLow
        leg := BULLISH_LEG
    leg

startOfNewLeg(int leg)      => ta.change(leg) != 0
startOfBearishLeg(int leg)  => ta.change(leg) == -1
startOfBullishLeg(int leg)  => ta.change(leg) == +1

drawLabel(int labelTime, float labelPrice, string tag, color labelColor, string labelStyle) =>    
    var label l_abel = na

    if modeInput == PRESENT
        l_abel.delete()

    l_abel := label.new(chart.point.new(labelTime,na,labelPrice),tag,xloc.bar_time,color=color(na),textcolor=labelColor,style = labelStyle,size = size.small)

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
 
drawStructure(pivot p_ivot, string tag, color structureColor, string lineStyle, string labelStyle, string labelSize) =>    
    var line l_ine      = line.new(na,na,na,na,xloc = xloc.bar_time)
    var label l_abel    = label.new(na,na)

    if modeInput == PRESENT
        l_ine.delete()
        l_abel.delete()

    l_ine   := line.new(chart.point.new(p_ivot.barTime,na,p_ivot.currentLevel), chart.point.new(time,na,p_ivot.currentLevel), xloc.bar_time, color=structureColor, style=lineStyle)
    l_abel  := label.new(chart.point.new(na,math.round(0.5*(p_ivot.barIndex+bar_index)),p_ivot.currentLevel), tag, xloc.bar_index, color=color(na), textcolor=structureColor, style=labelStyle, size = labelSize)

deleteOrderBlocks(bool internal = false) =>
    array<orderBlock> orderBlocks = internal ? internalOrderBlocks : swingOrderBlocks

    for [index,eachOrderBlock] in orderBlocks
        bool crossedOderBlock = false
        
        if bearishOrderBlockMitigationSource > eachOrderBlock.barHigh and eachOrderBlock.bias == BEARISH
            crossedOderBlock := true
           
        else if bullishOrderBlockMitigationSource < eachOrderBlock.barLow and eachOrderBlock.bias == BULLISH
            crossedOderBlock := true
           
        if crossedOderBlock                    
            orderBlocks.remove(index)            

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

        p_ivot.crossed := true
        t_rend.bias := BEARISH

        displayCondition = internal ? showInternalsInput and (showInternalBearInput == ALL or (showInternalBearInput == BOS and tag != CHOCH) or (showInternalBearInput == CHOCH and tag == CHOCH)) : showStructureInput and (showSwingBearInput == ALL or (showSwingBearInput == BOS and tag != CHOCH) or (showSwingBearInput == CHOCH and tag == CHOCH))
        
        if displayCondition                        
            drawStructure(p_ivot,tag,bearishColor,lineStyle,label.style_label_up,labelSize)

        if (internal and showInternalOrderBlocksInput) or (not internal and showSwingOrderBlocksInput)
            storeOrdeBlock(p_ivot,internal,BEARISH)


fairValueGapBox(leftTime,rightTime,topPrice,bottomPrice,boxColor) => box.new(chart.point.new(leftTime,na,topPrice),chart.point.new(rightTime + fairValueGapsExtendInput * (time-time[1]),na,bottomPrice), xloc=xloc.bar_time, border_color = boxColor, bgcolor = boxColor)

deleteFairValueGaps() =>
    for [index,eachFairValueGap] in fairValueGaps
        if (low < eachFairValueGap.bottom and eachFairValueGap.bias == BULLISH) or (high > eachFairValueGap.top and eachFairValueGap.bias == BEARISH)
            eachFairValueGap.topBox.delete()
            eachFairValueGap.bottomBox.delete()
            fairValueGaps.remove(index)
 
drawFairValueGaps() => 
    [lastClose, lastOpen, lastTime, currentHigh, currentLow, currentTime, last2High, last2Low] = request.security(syminfo.tickerid, fairValueGapsTimeframeInput, [close[1], open[1], time[1], high[0], low[0], time[0], high[2], low[2]],lookahead = barmerge.lookahead_on)

    barDeltaPercent     = (lastClose - lastOpen) / (lastOpen * 100)
    newTimeframe        = timeframe.change(fairValueGapsTimeframeInput)
    threshold           = fairValueGapsThresholdInput ? ta.cum(math.abs(newTimeframe ? barDeltaPercent : 0)) / bar_index * 2 : 0
    bullishFairValueGap = currentLow > last2High and lastClose > last2High and barDeltaPercent > threshold and newTimeframe
    bearishFairValueGap = currentHigh < last2Low and lastClose < last2Low and -barDeltaPercent > threshold and newTimeframe

    if bullishFairValueGap
        fairValueGaps.unshift(fairValueGap.new(currentLow,last2High,BULLISH,fairValueGapBox(lastTime,currentTime,currentLow,math.avg(currentLow,last2High),fairValueGapBullishColor),fairValueGapBox(lastTime,currentTime,math.avg(currentLow,last2High),last2High,fairValueGapBullishColor)))
    if bearishFairValueGap
        fairValueGaps.unshift(fairValueGap.new(currentHigh,last2Low,BEARISH,fairValueGapBox(lastTime,currentTime,currentHigh,math.avg(currentHigh,last2Low),fairValueGapBearishColor),fairValueGapBox(lastTime,currentTime,math.avg(currentHigh,last2Low),last2Low,fairValueGapBearishColor)))

getStyle(string style) =>
    switch style
        SOLID => line.style_solid
        DASHED => line.style_dashed
        DOTTED => line.style_dotted

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

higherTimeframe(string timeframe) => timeframe.in_seconds() > timeframe.in_seconds(timeframe)

updateTrailingExtremes() =>
    trailing.top            := math.max(high,trailing.top)
    trailing.lastTopTime    := trailing.top == high ? time : trailing.lastTopTime
    trailing.bottom         := math.min(low,trailing.bottom)
    trailing.lastBottomTime := trailing.bottom == low ? time : trailing.lastBottomTime

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


drawZone(float labelLevel, int labelIndex, float top, float bottom, string tag, color zoneColor, string style) =>
    var label l_abel    = label.new(na,na,text = tag, color=color(na),textcolor = zoneColor, style = style, size = size.small)
    var box b_ox        = box.new(na,na,na,na,bgcolor = color.new(zoneColor,80),border_color = color(na), xloc = xloc.bar_time)
    b_ox.set_top_left_point(    chart.point.new(trailing.barTime,na,top))
    b_ox.set_bottom_right_point(chart.point.new(last_bar_time,na,bottom))

    l_abel.set_point(           chart.point.new(na,labelIndex,labelLevel))


if showHighLowSwingsInput
    updateTrailingExtremes()

    if showHighLowSwingsInput
        drawHighLowSwings()

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