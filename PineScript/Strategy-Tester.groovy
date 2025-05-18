//@version=5
strategy("BB Strategy w/Toggle Switches & Leverage", overlay=true, initial_capital=300)

// ==== Inputs ====
bbLength = input.int(20, title="BB Length")
bbMult = input.float(2.0, title="BB Multiplier")
deployedCapital = input.float(15.0, title="Deployed Capital per Trade ($)")
leverage = input.float(20.0, title="Leverage")
tpPercent = input.float(10.0, title="Take Profit % of Capital") / 100   // Convert to decimal
slPercent = input.float(5.0, title="Stop Loss % of Capital") / 100      // Convert to decimal
buyEnabled = input.bool(true, "Enable Buy Trades", group="Strategy Controls")
sellEnabled = input.bool(true, "Enable Sell Trades", group="Strategy Controls")

// ==== Calculations ====
exposure = deployedCapital * leverage
adjustedTp = tpPercent / leverage
adjustedSl = slPercent / leverage

// ==== BB Touch Detection ====
getBBTouch(timeframe) =>
    [hhClose, hhUpper, hhLower] = request.security(syminfo.tickerid, timeframe, 
      [close, ta.sma(close, bbLength) + bbMult * ta.stdev(close, bbLength), 
       ta.sma(close, bbLength) - bbMult * ta.stdev(close, bbLength)])
    [hhClose >= hhUpper, hhClose <= hhLower]

// ==== Get Signals ====
[oneH_touchUpper, oneH_touchLower] = getBBTouch("60")
[fourH_touchUpper, fourH_touchLower] = getBBTouch("240")

buySignal = (oneH_touchLower or fourH_touchLower) and strategy.position_size == 0 and buyEnabled
sellSignal = (oneH_touchUpper or fourH_touchUpper) and strategy.position_size == 0 and sellEnabled

// ==== Risk Management ====
var float entryPrice = na
var float strictSL = na
var float strictTP = na

// Entry Logic
if buySignal
    entryPrice := close
    strictSL := entryPrice * (1 - adjustedSl)
    strictTP := entryPrice * (1 + adjustedTp)
    strategy.entry("Long", strategy.long, qty=exposure/close)
else if sellSignal
    entryPrice := close
    strictSL := entryPrice * (1 + adjustedSl)
    strictTP := entryPrice * (1 - adjustedTp)
    strategy.entry("Short", strategy.short, qty=exposure/close)

// Exit Logic
if strategy.position_size > 0  // Long
    strategy.exit("Long Exit", "Long", limit=strictTP, stop=strictSL)
else if strategy.position_size < 0  // Short
    strategy.exit("Short Exit", "Short", limit=strictTP, stop=strictSL)

// ==== Visuals ====
plotshape(buyEnabled ? buySignal : na, "BUY", shape.labelup, location.belowbar, color.new(#00FF00, 0), 0, "B")
plotshape(sellEnabled ? sellSignal : na, "SELL", shape.labeldown, location.abovebar, color.new(#FF0000, 0), 0, "S")
plot(strategy.position_size != 0 ? strictSL : na, "SL", color.red, 2, plot.style_linebr)
plot(strategy.position_size != 0 ? strictTP : na, "TP", color.green, 2, plot.style_linebr)