clear;
clc;
close;

FontS=24;
cd('./results/');

t=csvread('TimeValues.csv');
control=csvread('ControlInput.csv');
ratios=csvread('PopulationValues.csv');


aTc=control(:,1)/60;
IPTG=control(:,2)/.5;

figure();
plot(t,ratios(:,1),'r','linewidth',2);
hold on;
plot(t,ratios(:,2),'g','linewidth',2);
plot([t(1) t(end)],[0.5 0.5],'--','Color',[.5 0 0],'linewidth',2);
plot([t(1) t(end)],[0.5 0.5],'--','Color',[0 .5 0],'linewidth',2);
xlabel('t');
ylabel('ratios');
axis([0 t(end) 0 1]);
set(gca,'FontSize',FontS);

figure();
plot(t,0.5-ratios(:,1),'r','linewidth',2);
hold on;
plot(t,0.5-ratios(:,2),'g','linewidth',2);
plot([t(1) t(end)],[0 0],'Color',[0 0 0]);
xlabel('t');
ylabel('e');
axis([0 t(end) -1 1]);
set(gca,'FontSize',FontS);

figure();
plot(t,aTc,'r','linewidth',2);
hold on;
plot(t,IPTG,'g','linewidth',2);
xlabel('t');
ylabel('aTc/IPTG');
axis([0 t(end) 0 1]);
set(gca,'FontSize',FontS);



