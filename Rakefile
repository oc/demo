$LOAD_PATH.unshift(File.dirname(__FILE__) + '/lib')

require 'rubygems'
require 'bundler'
Bundler.setup
require 'thor'
require 'net/https'
require 'net/ssh'
require 'net/scp'
require 'open-uri'

class DeployHelper < Bundler::GemHelper
  # override
  def self.install_tasks(opts = nil)
    dir = caller.find { |c| /Rakefile:/ }[/^(.*?)\/Rakefile:/, 1]
    self.new(dir, opts && opts[:name]).install
  end

  # override
  def initialize(base, name = nil)
    Bundler.ui = Bundler::UI::Shell.new(Thor::Shell::Color.new)
    @base = base
  end

  # override
  def install
    [:production].each do |env|
      desc "=[ #{env.to_s.gsub('_', ' ').upcase} ]="
      task env do
        Bundler.ui.warn "Use: rake <env>:task"
      end
      namespace env do
        task :environment do
          @environment = env.to_s
        end

        desc "Deploy #{name} (#{version}) to #{env.to_s}"
        task :deploy => :environment do
          hosts.each do |host|
            pull_host(host)
            restart_host(host)
          end

          Bundler.ui.confirm "OK!"
        end

        desc "Pull #{name} (#{version}) to #{env.to_s}"
        task :pull => :environment do
          hosts.each do |host|
            pull_host(host)
          end
        end

        desc "Restart #{name} (#{version}) to #{env.to_s}"
        task :restart => :environment do
          hosts.each do |host|
            restart_host(host)
          end
        end

        desc "Start #{name} (#{version}) to #{env.to_s}"
        task :start => :environment do
          hosts.each do |host|
            start_host(host)
          end
        end

        desc "Stop #{name} (#{version}) to #{env.to_s}"
        task :stop => :environment do
          hosts.each do |host|
            stop_host(host)
          end
        end
      end
    end
  end

  # override
  def version
    @version ||= ENV['VERSION']
    @version ||= get_project_version
  end

  def get_project_version
    Bundler.ui.info "Enter what version you wish to deploy: "
    v = STDIN.gets
    v.strip
  end

  # override
  def name
    @name ||= config['application']['name']
  end

  def environment
    @environment
  end

  def artifact_url
    "#{config['repository']['url']}/#{config['application']['group_id'].gsub(".", "/")}/#{config['application']['artifact_id']}/#{version}/#{config['application']['artifact_id']}-#{version}.jar" #haq
  end

  def config
    @config ||= YAML::load_file(File.dirname(__FILE__) + '/config/config.yml')
  end

  def cfg(key)
    config[@environment][key.to_s]
  end

  # Convenience methods for reading configured values
  [:user, :prefix, :hosts, :database].each do |m|
    define_method m do
      cfg(m)
    end
  end

  ###
  ### Configure
  ###

  def pull_host(host)
    mark_previous(host)
    Net::SSH.start(host, user) do |ssh|
      Bundler.ui.info "Pulling #{artifact_url} to #{host}:#{prefix}/#{name}/#{name}-#{version}.jar..."
      ssh.exec("curl #{artifact_url} > #{prefix}/#{name}/#{name}-#{version}.jar")
    end
    mark_new(host)
  end

  def mark_previous(host)
    Net::SSH.start(host, user) do |ssh|
      Bundler.ui.info "Marking previous release on #{host}..."
      ssh.exec!("touch #{prefix}/#{name}/#{name}.jar") # In case it doesn't already exist
      ssh.exec!("mv #{prefix}/#{name}/#{name}.jar #{prefix}/#{name}/#{name}.jar.previous")
    end
  end

  def mark_new(host)
    Net::SSH.start(host, user) do |ssh|
      Bundler.ui.info "Symlinking new release..."
      ssh.exec("ln -sf #{prefix}/#{name}/#{name}-#{version}.jar #{prefix}/#{name}/#{name}.jar")
    end
  end

  def restart_host(host)
    Net::SSH.start(host, user) do |ssh|
      Bundler.ui.info "Restarting service on #{host}..."
      ssh.exec(cfg(:stop_command))
      ssh.exec(cfg(:start_command))
    end
  end

  def stop_host(host)
    Net::SSH.start(host, user) do |ssh|
      Bundler.ui.info "Stopping service on #{host}..."
      ssh.exec(cfg(:stop_command))
    end
  end

  def start_host(host)
    Net::SSH.start(host, user) do |ssh|
      Bundler.ui.info "Starting service on #{host}..."
      Bundler.ui.info "ssh.exec(#{cfg(:start_command)})"
      ssh.exec(cfg(:start_command))
    end
  end

end

DeployHelper.install_tasks

Dir["#{File.dirname(__FILE__)}/*/Rakefile"].each { |f| load f }