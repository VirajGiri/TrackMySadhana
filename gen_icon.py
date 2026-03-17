import math, io, numpy as np
from PIL import Image, ImageDraw
SIZE=512; cx=cy=256; S=SIZE/108.0
BG_CTR=(255,160,0); BG_EDGE=(191,54,12); CORD=(255,236,179)
GOLD=(255,193,7); GOLD_HI=(255,224,110); CREAM=(255,253,231)
AMBER=(255,143,0); DBROWN=(78,26,0); PETAL=(255,245,157)
RING_R=S*30; CORD_OUT=S*31; CORD_IN=S*29; BEAD_R=S*4; SUM_R=S*6
PET_L=S*13; PET_W=S*5.5; JEW_R=S*7.5; JEW_M=S*5.2; JEW_I=S*2.5
def circle(draw,x,y,r,fill): draw.ellipse([x-r,y-r,x+r,y+r],fill=fill)
yi,xi=np.mgrid[0:SIZE,0:SIZE]
dist=np.sqrt((xi-cx)**2+(yi-cy)**2)
t=np.clip(dist/(SIZE*0.55),0.0,1.0)
def lc(a,b): return (a*(1-t)+b*t).astype(np.uint8)
arr=np.stack([lc(BG_CTR[0],BG_EDGE[0]),lc(BG_CTR[1],BG_EDGE[1]),lc(BG_CTR[2],BG_EDGE[2]),np.full((SIZE,SIZE),255,dtype=np.uint8)],axis=2)
img=Image.fromarray(arr,'RGBA'); draw=ImageDraw.Draw(img)
cl=Image.new('RGBA',(SIZE,SIZE),(0,0,0,0)); cd=ImageDraw.Draw(cl)
cd.ellipse([cx-CORD_OUT,cy-CORD_OUT,cx+CORD_OUT,cy+CORD_OUT],fill=CORD)
cd.ellipse([cx-CORD_IN,cy-CORD_IN,cx+CORD_IN,cy+CORD_IN],fill=(0,0,0,0))
img=Image.alpha_composite(img,cl); draw=ImageDraw.Draw(img)
beads=[(cx+RING_R*math.sin(math.radians(i*30)),cy-RING_R*math.cos(math.radians(i*30))) for i in range(12)]
for i in range(1,12):
 bx,by=beads[i]; circle(draw,bx,by,BEAD_R,GOLD)
 hl=BEAD_R*0.45; draw.ellipse([bx-BEAD_R+2,by-BEAD_R+2,bx-BEAD_R+2+hl*2,by-BEAD_R+2+hl],fill=GOLD_HI)
sx,sy=beads[0]; circle(draw,sx,sy,SUM_R,CREAM)
draw.ellipse([sx-SUM_R+4,sy-SUM_R+3,sx-SUM_R+4+SUM_R,sy-SUM_R+3+SUM_R*0.45],fill=(255,255,248))
for i in range(6):
 a=math.radians(i*60); dx,dy=math.sin(a),-math.cos(a); px,py=math.cos(a),math.sin(a)
 L,W=PET_L,PET_W
 pts=[(cx-W*px,cy-W*py),(cx-W*0.4*px+0.5*L*dx,cy-W*0.4*py+0.5*L*dy),(cx+L*dx,cy+L*dy),(cx+W*0.4*px+0.5*L*dx,cy+W*0.4*py+0.5*L*dy),(cx+W*px,cy+W*py)]
 pl=Image.new('RGBA',(SIZE,SIZE),(0,0,0,0)); ImageDraw.Draw(pl).polygon(pts,fill=(*PETAL,230))
 img=Image.alpha_composite(img,pl)
draw=ImageDraw.Draw(img)
circle(draw,cx,cy,JEW_R,GOLD); circle(draw,cx,cy,JEW_M,AMBER); circle(draw,cx,cy,JEW_I,DBROWN)
out=r"D:\S T A R K\Viraj\GITHUB\TrackMySadhana\play_store_icon_512.png"
final=img.convert('RGBA')
buf=io.BytesIO(); final.save(buf,'PNG',optimize=True)
kb=len(buf.getvalue())/1024
print("Dimensions:",final.size[0],"x",final.size[1],"px")
print("File size :",round(kb,1),"KB")
final.save(out,'PNG',optimize=True)
print("Saved to  :",out)
