import math, io, numpy as np
from PIL import Image, ImageDraw, ImageFont
FW, FH = 1024, 500
BG_CTR=(255,160,0); BG_EDGE=(130,35,5)
CREAM=(255,253,231); GOLD=(255,193,7); AMBER=(255,143,0); DARK=(78,26,0)
AMB_LIGHT=(255,210,120)
# 1. Background gradient
yi,xi=np.mgrid[0:FH,0:FW]
gcx,gcy=FW*0.55,FH*0.45
dist=np.sqrt((xi-gcx)**2+(yi-gcy)**2)
t=np.clip(dist/(FW*0.65),0,1)
def lc(a,b): return (a*(1-t)+b*t).astype(np.uint8)
img=Image.fromarray(np.stack([lc(BG_CTR[0],BG_EDGE[0]),lc(BG_CTR[1],BG_EDGE[1]),lc(BG_CTR[2],BG_EDGE[2]),np.full((FH,FW),255,np.uint8)],axis=2),'RGBA')
# 2. Decorative mala ring LEFT
dcx,dcy,dr=205,250,162
for i in range(12):
 a=math.radians(i*30); bx=dcx+dr*math.sin(a); by=dcy-dr*math.cos(a)
 r=17 if i==0 else 12; col=(255,253,231,58) if i==0 else (255,193,7,52)
 bl=Image.new('RGBA',(FW,FH),(0,0,0,0)); ImageDraw.Draw(bl).ellipse([bx-r,by-r,bx+r,by+r],fill=col)
 img=Image.alpha_composite(img,bl)
cl=Image.new('RGBA',(FW,FH),(0,0,0,0)); cd=ImageDraw.Draw(cl)
cd.ellipse([dcx-dr-4,dcy-dr-4,dcx+dr+4,dcy+dr+4],outline=(255,220,130,38),width=4)
img=Image.alpha_composite(img,cl)
# Lotus at deco centre
for i in range(6):
 a=math.radians(i*60); dx2,dy2=math.sin(a),-math.cos(a); px2,py2=math.cos(a),math.sin(a)
 L2,PW=40,7
 pts=[(dcx-PW*px2,dcy-PW*py2),(dcx-PW*0.4*px2+0.5*L2*dx2,dcy-PW*0.4*py2+0.5*L2*dy2),(dcx+L2*dx2,dcy+L2*dy2),(dcx+PW*0.4*px2+0.5*L2*dx2,dcy+PW*0.4*py2+0.5*L2*dy2),(dcx+PW*px2,dcy+PW*py2)]
 pl=Image.new('RGBA',(FW,FH),(0,0,0,0)); ImageDraw.Draw(pl).polygon(pts,fill=(255,245,157,48))
 img=Image.alpha_composite(img,pl)
draw=ImageDraw.Draw(img)
draw.ellipse([dcx-16,dcy-16,dcx+16,dcy+16],fill=(255,193,7,90))
draw.ellipse([dcx-8,dcy-8,dcx+8,dcy+8],fill=(255,143,0,110))
draw.ellipse([dcx-4,dcy-4,dcx+4,dcy+4],fill=(78,26,0,180))
# Small deco ring TOP-RIGHT corner
dcx2,dcy2,dr2=950,70,60
for i in range(12):
 a=math.radians(i*30); bx=dcx2+dr2*math.sin(a); by=dcy2-dr2*math.cos(a)
 r=7 if i==0 else 5; col=(255,253,231,30) if i==0 else (255,193,7,28)
 bl=Image.new('RGBA',(FW,FH),(0,0,0,0)); ImageDraw.Draw(bl).ellipse([bx-r,by-r,bx+r,by+r],fill=col)
 img=Image.alpha_composite(img,bl)
# Small deco ring BOTTOM-RIGHT corner
dcx3,dcy3,dr3=980,440,48
for i in range(12):
 a=math.radians(i*30); bx=dcx3+dr3*math.sin(a); by=dcy3-dr3*math.cos(a)
 r=5 if i==0 else 4; col=(255,253,231,28) if i==0 else (255,193,7,25)
 bl=Image.new('RGBA',(FW,FH),(0,0,0,0)); ImageDraw.Draw(bl).ellipse([bx-r,by-r,bx+r,by+r],fill=col)
 img=Image.alpha_composite(img,bl)
# 3. Vertical divider between deco and text
div_layer=Image.new('RGBA',(FW,FH),(0,0,0,0))
ImageDraw.Draw(div_layer).line([(380,40),(380,460)],fill=(255,193,7,60),width=1)
img=Image.alpha_composite(img,div_layer)
draw=ImageDraw.Draw(img)
# 4. Fonts
try:
 f_title=ImageFont.truetype("C:/Windows/Fonts/arialbd.ttf",62)
 f_sub=ImageFont.truetype("C:/Windows/Fonts/arialbd.ttf",26)
 f_feat=ImageFont.truetype("C:/Windows/Fonts/arial.ttf",19)
 f_tag=ImageFont.truetype("C:/Windows/Fonts/ariali.ttf",23)
 f_sm=ImageFont.truetype("C:/Windows/Fonts/arial.ttf",15)
except:
 f_title=f_sub=f_feat=f_tag=f_sm=ImageFont.load_default()
tx=400
# 5. App name with drop-shadow
draw.text((tx+2,82),"TrackMySadhana",font=f_title,fill=(60,15,0))
draw.text((tx,80),"TrackMySadhana",font=f_title,fill=CREAM)
# Subtitle
draw.text((tx,158),"Daily Jap & Mantra Companion",font=f_sub,fill=AMB_LIGHT)
# Gold divider
draw.line([(tx,200),(1008,200)],fill=GOLD,width=2)
# 6. Feature pills
feats=["Mala Counter","Progress Reports","Daily History"]
for i,f in enumerate(feats):
 px3=tx+i*206; py3=216
 pl2=Image.new('RGBA',(FW,FH),(0,0,0,0))
 ImageDraw.Draw(pl2).rounded_rectangle([px3,py3,px3+195,py3+42],radius=21,fill=(0,0,0,62))
 img=Image.alpha_composite(img,pl2); draw=ImageDraw.Draw(img)
 draw.text((px3+12,py3+11),f,font=f_feat,fill=CREAM)
# Secondary features
draw.text((tx,274),"Streak Tracker  |  Experience Notes  |  Sub-Mantra Support",font=f_feat,fill=(255,210,120))
# Thin divider 2
draw.line([(tx,306),(1008,306)],fill=(255,193,7,80),width=1)
# Tagline
draw.text((tx,318),"Stay consistent. Stay devoted.",font=f_tag,fill=(255,240,200))
# Gold dot row
for i in range(7):
 draw.ellipse([tx+i*18,360,tx+i*18+10,370],fill=GOLD)
# OM text (unicode - may render as box if font lacks it, that is OK)
draw.text((tx,388),"  Om Namah Shivaya",font=f_sub,fill=(255,193,7))
# Watermark
draw.text((tx,460),"trackmysadhana.app",font=f_sm,fill=(255,220,150))
# Save
out=r"D:\S T A R K\Viraj\GITHUB\TrackMySadhana\play_store_feature_graphic.png"
final=img.convert('RGBA')
buf=io.BytesIO(); final.save(buf,'PNG',optimize=True)
kb=len(buf.getvalue())/1024
print("Dimensions:",FW,"x",FH)
print("File size :",round(kb,1),"KB")
final.save(out,'PNG',optimize=True)
print("Saved:",out)
