clear;
clc;
close;

cd('./results');

t=csvread('TimeValues.csv');
% bState=csvread('BacteriaValues.csv');
% LacI=bState(:,3:4:end);
% TetR=bState(:,4:4:end);
control=csvread('ControlInput.csv');
ratios=csvread('PopulationValues.csv');

cd('./images/');


% for i=1:length(t)
%     av_LacI(i)=mean(LacI(i,LacI(i,:)>0));
%     av_TetR(i)=mean(TetR(i,TetR(i,:)>0));
%     celln(i)=length(LacI(i,LacI(i,:)>0));
% end



aTc=control(:,1)/60;
IPTG=control(:,2)/0.5;

video=VideoWriter('Bang_Bang_BSim');
video.FrameRate=24;
video.open();

[img,rect]=imcrop(imread('0.00.png'));
close;

%% 
init_F=1;
f=figure('pos',[100 100 1200 1200*3/4]);
for i=init_F:length(t)
    image=imcrop(imread(sprintf('%d.00.png',5*60*(i-1))),rect);
    
%     subplot(2,3,[2 5]);
    subplot(2,2,[1 3]);
    imshow(image);
    title('Growth Chamber');
    set(gca,'fontsize',18);
    
    
%     subplot(2,3,3);
%     plot(t(init_F:i),celln(init_F:i));
%     axis([t(init_F) t(end) min(celln) max(celln)]);
%     xlabel('t');
%     ylabel('Cell Number');
%     set(gca,'fontsize',18);
    
    
%     subplot(2,3,6);
%     plot(t(init_F:i),av_GFP(init_F:i),'color',[0 .5 0],'linewidth',2);
%     hold on;
%     plot(t(init_F:i),IPTG(init_F:i),'color',[0.5 0 0],'linewidth',2);
%     axis([t(init_F) t(end) min(av_GFP) max(av_GFP)]);
%     xlabel('t');
%     ylabel('GFP,IPTG');
%     set(gca,'fontsize',18);
%     legend('GFP','IPTG');

    
%     subplot(2,3,1);
    subplot(2,2,2);
    hold on;
    plot(t(init_F:i),ratios(init_F:i,1),'r','linewidth',2);
    plot(t(init_F:i),ratios(init_F:i,2),'g','linewidth',2);
    plot([t(init_F) t(i)],[0.75 0.75],'Color',[.5 0 0],'linewidth',2);
    plot([t(init_F) t(i)],[0.25 0.25],'Color',[0 .5 0],'linewidth',2);
    axis([t(init_F) t(end) 0 1]);
    xlabel('t');
    ylabel('Ratios');
    legend1=legend('r_B','r_A','r_d','1-r_d');
    set(legend1,'Location','northeastoutside');
    set(gca,'fontsize',18);

%     subplot(2,3,4);
    subplot(2,2,4);
    hold on;
    plot(t(init_F:i),control(init_F:i,1)/100,'r','linewidth',2);
    plot(t(init_F:i),control(init_F:i,2),'g','linewidth',2);
    axis([t(init_F) t(end) 0 1]);
    xlabel('t');
    ylabel('Control Inputs');
    legend1=legend('u_{aTc}/100','u_{IPTG}');
    set(legend1,'Location','northeastoutside');
    set(gca,'fontsize',18);
    
    
    
    video.writeVideo(getframe(f));
    clf(f);
    
end
close;
video.close();
